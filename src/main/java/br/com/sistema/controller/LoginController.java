package br.com.sistema.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sistema.dto.security.CredenciaisDto;
import br.com.sistema.service.LoginService;

@RestController
@RequestMapping("/auth")
public class LoginController {

	private Logger logger = Logger.getLogger(LoginController.class.getName());
	
	@Autowired
	private LoginService loginService;
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/login") // Define que este método será chamado quando houver uma requisição HTTP POST para o endpoint "/login"
	public ResponseEntity signin(@RequestBody CredenciaisDto loginRequestDTO) { // Método de login que recebe as credenciais no corpo da requisição
	    logger.info("Método signin chamado."); // Registra no log que o método de login foi chamado
	    if (validaParametros(loginRequestDTO)) { // Verifica se os parâmetros da requisição são válidos
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Parâmetros inválidos."); // Se os parâmetros forem inválidos, retorna status 403 (FORBIDDEN) e mensagem de erro
	    }
	    var token = loginService.signin(loginRequestDTO); // Chama o serviço de login para gerar um token com as credenciais fornecidas
	    if (token == null) { // Verifica se o token gerado é nulo, indicando falha no login
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token inválidos."); // Se o token for nulo, retorna status 403 e mensagem de erro
	    }
	    return token; // Se o login for bem-sucedido, retorna o token gerado
	}

	private boolean validaParametros(CredenciaisDto loginRequestDTO) { // Método auxiliar que valida se os parâmetros de login são válidos
	    logger.info("Método validaParâmetros chamado."); // Registra no log que a validação dos parâmetros foi chamada
	    return loginRequestDTO == null // Verifica se o objeto de credenciais é nulo
	        || loginRequestDTO.getUsername() == null // Verifica se o nome de usuário é nulo
	        || loginRequestDTO.getUsername().isBlank() // Verifica se o nome de usuário está em branco
	        || loginRequestDTO.getPassword() == null // Verifica se a senha é nula
	        || loginRequestDTO.getPassword().isBlank(); // Verifica se a senha está em branco
	}
}
