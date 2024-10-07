package br.com.cnsoftware.sistema.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.cnsoftware.sistema.controller.dto.LoginRequestDTO;
import br.com.cnsoftware.sistema.controller.dto.LoginResponseDTO;
import br.com.cnsoftware.sistema.model.entity.Usuario;
import br.com.cnsoftware.sistema.repository.UsuarioRepository;
import jakarta.validation.Valid;

@Service // Implementar a Classe UserDetailsService para o Spring Security saber que deve chamar de forma automática
public class AutorizacaoService implements UserDetailsService {

	private AuthenticationManager authenticationManager;
	
	@Autowired
    private ApplicationContext context;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
    private TokenService tokenService;

//	@Override	// Fernanda Kipper - Modo 01 // Quando o findByLogin retornar um UserDetail
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		return usuarioRepository.findByLogin(username); 
//	}

	@Override	// Fernanda Kipper - Modo 02 // Quando o findByLogin retornar um Optional<Usuário>
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = this.usuarioRepository.findByLogin(username).orElseThrow( () -> new UsernameNotFoundException("Usuário não foi encontrado.") );
		User user = new User(usuario.getUsername(), usuario.getSenha(), usuario.getAuthorities());
		return user;
	}

	public ResponseEntity<Object> login( @RequestBody @Valid LoginRequestDTO requestData ) {
		
		Usuario usuario = this.usuarioRepository.findByEmail(requestData.login()).orElseThrow(() -> new RuntimeException("User not found"));
        PasswordEncoder passwordEncoder = null;
        
		if(passwordEncoder.matches(requestData.senha(), usuario.getSenha())) {
            String token = this.tokenService.gerarToken(usuario);
            return ResponseEntity.ok(new LoginResponseDTO(usuario.getNome(), token));
        }
        return ResponseEntity.badRequest().build();
	}

	
	public ResponseEntity<Object> register( @RequestBody @Valid LoginRequestDTO requestData ) {
		
		return ResponseEntity.ok(new LoginResponseDTO("", ""));
	}
	
	
	
	
	
}
