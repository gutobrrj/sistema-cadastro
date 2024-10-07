package br.com.cnsoftware.sistema.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cnsoftware.sistema.controller.dto.LoginRequestDTO;
import br.com.cnsoftware.sistema.controller.dto.RegisterRequestDTO;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

	@Autowired
	private AutorizacaoService autorizacaoService;
	
	
	@PostMapping("/login")	
	public ResponseEntity<Object> login( @RequestBody LoginRequestDTO requestData ) {
		return autorizacaoService.login(requestData);
	}
	
	
//	@PostMapping("/register")	
//	public ResponseEntity<Object> registrar( @RequestBody RegisterRequestDTO requestData ) {
//		return autorizacaoService.registrar(requestData);
//	}
	
}
