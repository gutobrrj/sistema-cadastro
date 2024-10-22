package br.com.sistema.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import br.com.sistema.dto.security.TokenResponseDto;
import br.com.sistema.exceptions.AutenticacaoJwtInvalidaException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenService {

	private Logger logger = Logger.getLogger(TokenService.class.getName());
	
	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validadeToken = 3600000;

	@Autowired
	private UserDetailsService userDetailsService;

	Algorithm algorithm = null;

	
	@PostConstruct
	protected void init() { // Método que inicializa a secretKey e o algoritmo de criptografia HMAC256 após a construção do bean
	    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes()); // Codifica a chave secreta em Base64
	    algorithm = Algorithm.HMAC256(secretKey.getBytes()); // Define o algoritmo HMAC256 usando a chave secreta
	}

	
	// ###### METODO QUE CRIA E RETORNA UM TOKEN DE ACESSO APÓS CONFERIR AS INFORMACOES DE LOGIN ######
	public TokenResponseDto criaTokenDeAcesso(String username, List<String> roles) { 
		logger.info("8. Iniciando a criação do Token de Acesso."); 
	    try {
	    	Date agora = new Date(); // Data atual (momento da geração do token)
		    Date validade = new Date(agora.getTime() + validadeToken); // Data de expiração do token (com base na validade do token)
		    String accessToken = gerarAccessToken(username, roles, agora, validade); // Gera o token de acesso com os dados do usuário
		    logger.info("10. Finalizando e retornando Token para o usuário."); 
		    return new TokenResponseDto(username, true, agora, validade, accessToken); // Retorna o TokenDto com o access token e suas informações
		} catch (JWTCreationException e) {
			throw new RuntimeException("Erro no processo de geração do token.");
		}
	}


	// ###### METODO AUXILIAR QUE CRIA O ACCESS TOKEN ######
	private String gerarAccessToken(String username, List<String> roles, Date agora, Date validade) { 
		logger.info("9. Gerando Token"); 
	    String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString(); // Obtém a URL do emissor do token (normalmente a URL da aplicação)
	    return JWT.create()
	    		.withClaim("roles", roles) 						// Adiciona as roles do usuário ao token
	            .withIssuedAt(agora).withExpiresAt(validade) 	// Define a data de emissão e expiração do token
	            .withSubject(username).withIssuer(issuerUrl) 	// Define o usuário e o emissor do token
	            .sign(algorithm).strip(); 						// Assina o token com o algoritmo HMAC256 e remove qualquer espaço em branco ao redor
	}


	// ###### Método que retorna os dados de autenticação do usuário com base no token fornecido ######
	public Authentication obterAutenticacaoUsuario(String token) { 
		logger.info("5. Realizando autenticação com username/password obtidos da requisição."); 
	    DecodedJWT decodedJWT = decodificaToken(token); // Decodifica o token JWT
	    UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject()); // Carrega os detalhes do usuário com base no nome de usuário decodificado do token
	    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()); // Cria e retorna um objeto de autenticação com os dados do usuário e suas permissões (authorities)
	}

	
	// ###### METODO QUE PEGA O TOKEN ENVIADO PELA REQUISIÇÃO ######
	public String getAuthorizationRequisicao(HttpServletRequest request) { // Método utilizado/chamado no TokenFilter
		logger.info("2. Análise da Requisição iniciada. Método getTokenRequisicao analisando o header em busca de Authorization."); 
	    String bearerToken = request.getHeader("Authorization"); // Obtém o cabeçalho "Authorization" da requisição
	    if(bearerToken != null && bearerToken.startsWith("Bearer ")) { // Verifica se o token está presente e começa com "Bearer "
	        return bearerToken.substring("Bearer ".length()); // Retorna o token sem o prefixo "Bearer "
	    }
	    logger.info("3. Authorization não encontrado. Retornando null.");
	    return null; // Se a requisição http não possuir Authorizarion e o token não estiver presente ou não seguir o formato esperado, retorna null
	}


	// ###### METODO QUE VERIFICA SE O TOKEN INFORMADO É VALIDO ######
	public boolean validarToken(String token) { // Método utilizado/chamado no TokenFilter
		logger.info("3. Requisição possui Authorization no Header. Token sendo validado."); 
	    DecodedJWT decodedJWT = decodificaToken(token); // Decodifica o token JWT
	    try {
	        if(decodedJWT.getExpiresAt().before(new Date())) { // Verifica se o token está expirado
	        	logger.info("Token expirado. Retornando false."); 
	            return false; // Retorna false se o token já expirou
	        }
	        return true; // Retorna true se o token for válido
	    } catch (Exception e) {
	        throw new AutenticacaoJwtInvalidaException("Token inválido ou expirado"); // Lança uma exceção se houver algum problema na validação do token
	    }
	}
	
	
	// ###### METODO AUXILIAR PARA DECODIFICAR UM TOKEN JWT ###### 
	private DecodedJWT decodificaToken(String token) { // Utilizado no processo de login
		logger.info("Decodificando Token obtido do Autorization Header"); 
	    Algorithm alg = Algorithm.HMAC256(secretKey.getBytes()); 	// Define o algoritmo HMAC256 com a chave secreta
	    JWTVerifier verifier =  JWT.require(alg).build(); 			// Cria um verificador JWT com o algoritmo
	    DecodedJWT decodedJWT = verifier.verify(token); 			// Verifica e decodifica o token JWT
	    return decodedJWT; 											// Retorna o token decodificado
	}

}
