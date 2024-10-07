package br.com.cnsoftware.sistema.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autorizacao")
public class TestaAutorizacao {

	@GetMapping
	public ResponseEntity<String> getAutorizacao() {
		return ResponseEntity.ok("Requisição Autorizada!");
	}
	
}
