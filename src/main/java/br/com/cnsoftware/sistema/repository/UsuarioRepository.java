package br.com.cnsoftware.sistema.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cnsoftware.sistema.model.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findByEmail(String email);
	
	Optional<Usuario> findByLogin(String login);
	
//	UserDetails findByLogin(String login);
	
	Optional<Usuario> findByLoginOrEmail(String login, String email);
	
}
