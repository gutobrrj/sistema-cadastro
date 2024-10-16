package br.com.sistema.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import br.com.sistema.model.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findByEmail(String email);
	
//	Optional<UserDetails> findByUsername(String username);
	Optional<Usuario> findByUsername(String username);
	
	Optional<Usuario> findByUsernameOrEmail(String username, String email);
	
}
