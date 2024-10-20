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
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import br.com.sistema.dto.security.TokenDto;
import br.com.sistema.exceptions.AutenticacaoJwtInvalidaException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenService {

	private Logger logger = Logger.getLogger(SecurityConfig.class.getName());
	
	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validadeToken = 3600000;

	@Autowired
	private UserDetailsService userDetailsService;

	Algorithm algorithm = null;

	public TokenService() {
		logger.info("Iniciando LoginService");
	}
	
	@PostConstruct
	protected void init() { // Método que inicializa o segredo e o algoritmo de criptografia HMAC256 após a construção do bean
	    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes()); // Codifica a chave secreta em Base64
	    algorithm = Algorithm.HMAC256(secretKey.getBytes()); // Define o algoritmo HMAC256 usando a chave secreta
	}

	public TokenDto criarAccessToken(String username, List<String> roles) { // Método que cria e retorna um token de acesso (access token) e um refresh token
	    Date agora = new Date(); // Data atual (momento da geração do token)
	    Date validade = new Date(agora.getTime() + validadeToken); // Data de expiração do token (com base na validade do token)
	    var accessToken = obterAccessToken(username, roles, agora, validade); // Gera o token de acesso com os dados do usuário
	    var refreshToken = obterRefreshToken(username, roles, agora); // Gera o refresh token
	    return new TokenDto(username, true, agora, validade, accessToken, refreshToken); // Retorna o TokenDto com o access token, refresh token e suas informações
	}

	public TokenDto refreshToken(String refreshToken) { // Método que gera um novo access token usando o refresh token
	    if (refreshToken.contains("Bearer ")) refreshToken = refreshToken.substring("Bearer ".length()); // Remove o prefixo "Bearer " do refresh token, se presente
	    JWTVerifier verifier = JWT.require(algorithm).build(); // Cria um verificador JWT com o algoritmo HMAC256
	    DecodedJWT decodedJWT = verifier.verify(refreshToken); // Verifica e decodifica o refresh token
	    String username = decodedJWT.getSubject(); // Obtém o nome de usuário do token decodificado
	    List<String> roles = decodedJWT.getClaim("roles").asList(String.class); // Obtém as roles do usuário do token decodificado
	    return criarAccessToken(username, roles); // Gera e retorna um novo access token com base nos dados decodificados
	}

	private String obterAccessToken(String username, List<String> roles, Date agora, Date validade) { // Método que cria o access token
	    String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString(); // Obtém a URL do emissor do token (normalmente a URL da aplicação)
	    return JWT.create().withClaim("roles", roles) // Adiciona as roles do usuário ao token
	            .withIssuedAt(agora).withExpiresAt(validade) // Define a data de emissão e expiração do token
	            .withSubject(username).withIssuer(issuerUrl) // Define o usuário e o emissor do token
	            .sign(algorithm).strip(); // Assina o token com o algoritmo HMAC256 e remove qualquer espaço em branco ao redor
	}

	private String obterRefreshToken(String username, List<String> roles, Date agora) { // Método que cria o refresh token
	    Date validadeRefreshToken = new Date(agora.getTime() + (validadeToken * 3)); // Define a validade do refresh token (geralmente maior que o access token)
	    return JWT.create().withClaim("roles", roles) // Adiciona as roles do usuário ao refresh token
	            .withIssuedAt(agora).withExpiresAt(validadeRefreshToken) // Define a data de emissão e expiração do refresh token
	            .withSubject(username) // Define o usuário no token
	            .sign(algorithm).strip(); // Assina o token com o algoritmo HMAC256 e remove espaços ao redor
	}

	public Authentication getAuthentication(String token) { // Método que retorna os dados de autenticação do usuário com base no token fornecido
	    DecodedJWT decodedJWT = decodificarToken(token); // Decodifica o token JWT
	    UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject()); // Carrega os detalhes do usuário com base no nome de usuário decodificado do token
	    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()); // Cria e retorna um objeto de autenticação com os dados do usuário e suas permissões (authorities)
	}

	private DecodedJWT decodificarToken(String token) { // Método que decodifica um token JWT
	    Algorithm alg = Algorithm.HMAC256(secretKey.getBytes()); // Define o algoritmo HMAC256 com a chave secreta
	    JWTVerifier verifier =  JWT.require(alg).build(); // Cria um verificador JWT com o algoritmo
	    DecodedJWT decodedJWT = verifier.verify(token); // Verifica e decodifica o token JWT
	    return decodedJWT; // Retorna o token decodificado
	}

	public String recuperaToken(HttpServletRequest request) { // Método que recupera o token da requisição HTTP
	    String bearerToken = request.getHeader("Authorization"); // Obtém o cabeçalho "Authorization" da requisição
	    if(bearerToken != null && bearerToken.startsWith("Bearer ")) { // Verifica se o token está presente e começa com "Bearer "
	        return bearerToken.substring("Bearer ".length()); // Retorna o token sem o prefixo "Bearer "
	    }
	    return null; // Se o token não estiver presente ou não seguir o formato esperado, retorna null
	}

	public boolean validarToken(String token) { // Método que valida se o token JWT é válido
	    DecodedJWT decodedJWT = decodificarToken(token); // Decodifica o token JWT
	    try {
	        if(decodedJWT.getExpiresAt().before(new Date())) { // Verifica se o token está expirado
	            return false; // Retorna false se o token já expirou
	        }
	        return true; // Retorna true se o token for válido
	    } catch (Exception e) {
	        throw new AutenticacaoJwtInvalidaException("Token inválido ou expirado"); // Lança uma exceção se houver algum problema na validação do token
	    }
	}

}
