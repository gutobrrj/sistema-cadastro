package br.com.cnsoftware.sistema.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cnsoftware.sistema.controller.dto.LoginRequestDTO;
import br.com.cnsoftware.sistema.controller.dto.RegisterRequestDTO;
import br.com.cnsoftware.sistema.security.service.AutenticacaoService;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

	@Autowired
	private AutenticacaoService autorizacaoService;
	
	
	@PostMapping("/login")	
	public ResponseEntity<Object> login( @RequestBody LoginRequestDTO requestData ) {
		return autorizacaoService.login(requestData);
	}
	
	
	@PostMapping("/register")	
	public ResponseEntity<Object> registrar( @RequestBody RegisterRequestDTO requestData ) {
		return autorizacaoService.register(requestData);
	}
	
}
