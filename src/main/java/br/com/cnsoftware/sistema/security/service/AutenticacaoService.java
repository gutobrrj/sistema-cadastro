package br.com.cnsoftware.sistema.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.cnsoftware.sistema.controller.dto.LoginRequestDTO;
import br.com.cnsoftware.sistema.controller.dto.RegisterRequestDTO;
import br.com.cnsoftware.sistema.controller.dto.ResponseDTO;
import br.com.cnsoftware.sistema.model.entity.Usuario;
import br.com.cnsoftware.sistema.model.entity.enums.RoleUsuario;
import br.com.cnsoftware.sistema.repository.UsuarioRepository;
import br.com.cnsoftware.sistema.security.TokenService;
import jakarta.validation.Valid;

@Service // Implementar a Classe UserDetailsService para o Spring Security saber que deve chamar de forma automática
public class AutenticacaoService implements UserDetailsService {

	@Lazy
	@Autowired
 	private AuthenticationManager authenticationManager; 	//Não utilizar @Autowired para não ocorrer e corrigir a 'circular references'
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
    private TokenService tokenService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws InternalAuthenticationServiceException {
		return usuarioRepository.findByLogin(username); 
	}

	

	public ResponseEntity<Object> login( @RequestBody @Valid LoginRequestDTO requestData ) {
		try {
			var usernamePasswordAuthenticationToken =  new UsernamePasswordAuthenticationToken(requestData.login(), requestData.senha());
			var authenticate = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
			
			var usuario = (Usuario)authenticate.getPrincipal();
			var token = tokenService.gerarToken(usuario);
	        return ResponseEntity.ok(new ResponseDTO(usuario.getNome(), token));
	        
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário inexistente ou senha inválida");
		}
	}

	
	public ResponseEntity<Object> register( @RequestBody @Valid RegisterRequestDTO requestData ) {
		if( usuarioRepository.findByLogin(requestData.login()) != null ) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login já cadastrado."); }
		if( usuarioRepository.findByEmail(requestData.email()) != null ) { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email já cadastrado."); }
		Usuario novoUsuario = new Usuario();
		novoUsuario.setNome(requestData.nome());
		novoUsuario.setEmail(requestData.email());
		novoUsuario.setLogin(requestData.login());
		novoUsuario.setSenha(new BCryptPasswordEncoder().encode(requestData.senha()));
		novoUsuario.setRole( requestData.role().equalsIgnoreCase("ADMIN") ? RoleUsuario.ADMIN : RoleUsuario.USUARIO );
		this.usuarioRepository.save(novoUsuario);
		String token = this.tokenService.gerarToken(novoUsuario);
		return ResponseEntity.ok(new ResponseDTO(novoUsuario.getNome(), token));
	}
	
	
}
