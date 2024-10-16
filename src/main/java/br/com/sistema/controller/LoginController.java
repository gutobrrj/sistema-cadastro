package br.com.sistema.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sistema.controller.dto.LoginRequestDTO;
import br.com.sistema.controller.dto.LoginResponseDTO;
import br.com.sistema.exceptions.DadosInvalidosException;
import br.com.sistema.exceptions.UsuarioNaoEncontradoException;
import br.com.sistema.model.entity.Usuario;
import br.com.sistema.repository.UsuarioRepository;
import br.com.sistema.service.TokenService;

@RestController
@RequestMapping("/auth")
public class LoginController {

	private UsuarioRepository usuarioRepository;
	private PasswordEncoder passwordEncoder;
	private TokenService tokenService;
	
	public LoginController(UsuarioRepository usuarioRepository,PasswordEncoder passwordEncoder,TokenService tokenService) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenService = tokenService;
	}

	@PostMapping("/login")
	public ResponseEntity<Object> authenticate(@RequestBody LoginRequestDTO loginRequestDTO) {
		Usuario usuario = this.usuarioRepository.findByUsername(loginRequestDTO.username()).orElseThrow( () -> new UsuarioNaoEncontradoException("Username não localizado.") );
		if(passwordEncoder.matches(loginRequestDTO.password(), usuario.getPassword())) {
			String token = this.tokenService.generateToken(usuario);
			return ResponseEntity.ok(new LoginResponseDTO(usuario.getUsername(), token));
		} else {
			throw new DadosInvalidosException("Senha incorreta. Tente novamente.");
		}
	}
}
