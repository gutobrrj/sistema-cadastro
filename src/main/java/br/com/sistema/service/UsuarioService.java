package br.com.sistema.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.sistema.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
	
	private Logger logger = Logger.getLogger(UsuarioService.class.getName());

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Buscando pelo usuário: " + username);
		var user = usuarioRepository.findByUsername(username);
		if(user != null) {
			return user;
		} else {
			throw new UsernameNotFoundException("Usuário " + username + " não encontrado");
		}
	}
    

}
