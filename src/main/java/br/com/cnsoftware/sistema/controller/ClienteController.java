package br.com.cnsoftware.sistema.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.cnsoftware.sistema.model.entity.Cliente;
import br.com.cnsoftware.sistema.service.ClienteService;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	private ClienteService service;
	
	@PostMapping
	public ResponseEntity<Cliente> salvar( @RequestBody Cliente cliente) {
		Cliente clienteSalvo = service.salvar(cliente);
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
	}
	
	@GetMapping
	public ResponseEntity<List<Cliente>> listarTodos() {
		List<Cliente> clientes = service.listarTodos();
		return ResponseEntity.status(HttpStatus.OK).body(clientes);
	}
}
