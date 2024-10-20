package br.com.sistema.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import br.com.sistema.dto.security.CredenciaisDto;
import br.com.sistema.dto.security.TokenDto;
import br.com.sistema.exceptions.DadosInvalidosException;
import br.com.sistema.exceptions.UsuarioNaoEncontradoException;
import br.com.sistema.repository.UsuarioRepository;
import br.com.sistema.security.TokenService;

@Service
public class LoginService {

	private Logger logger = Logger.getLogger(LoginService.class.getName());
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenProvider;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public ResponseEntity<TokenDto> signin(CredenciaisDto data) { // Método para autenticar o usuário e gerar um token de acesso
	    logger.info("Iniciando metodo signin"); // Registra no log que o método de autenticação foi iniciado
	    try {
	        var username = data.getUsername(); // Obtém o nome de usuário das credenciais fornecidas
	        var password = data.getPassword(); // Obtém a senha das credenciais fornecidas
	        authenticationManager.authenticate( // Autentica o usuário com base no nome de usuário e senha
	            new UsernamePasswordAuthenticationToken(username, password)
	        );
	        var usuario = usuarioRepository.findByUsername(username); // Busca o usuário no banco de dados usando o nome de usuário
	        var tokenResponse = new TokenDto(); // Cria um objeto vazio de TokenDto para armazenar o token de resposta
	        
	        if (usuario != null) { // Verifica se o usuário foi encontrado no banco de dados
	            tokenResponse = tokenProvider.criarAccessToken(username, usuario.getRoles()); // Gera um token de acesso se o usuário for encontrado, usando o nome de usuário e suas permissões (roles)
	        } else {
	            throw new UsuarioNaoEncontradoException("Usuário " + username + " não encontrado."); // Se o usuário não for encontrado, lança uma exceção informando o erro
	        }
	        
	        return ResponseEntity.ok(tokenResponse); // Retorna uma resposta HTTP 200 (OK) com o token gerado
	    } catch (Exception e) { // Captura qualquer exceção que ocorra durante o processo de autenticação
	        throw new DadosInvalidosException("usuário ou senha inválidos."); // Lança uma exceção informando que os dados de autenticação são inválidos
	    }
	}

}
