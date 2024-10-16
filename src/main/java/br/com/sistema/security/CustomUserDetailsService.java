package br.com.sistema.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.sistema.model.entity.Usuario;
import br.com.sistema.repository.UsuarioRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = this.usuarioRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException("Usuário2 '" + username + "' não encontrado.") );
		return new User(usuario.getUsername(), usuario.getPassword(), new ArrayList<>());
	}

}
