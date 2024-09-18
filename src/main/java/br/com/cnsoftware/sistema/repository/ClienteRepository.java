package br.com.cnsoftware.sistema.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.cnsoftware.sistema.model.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	Optional<Cliente> findByCpfCnpj(String cpfCnpj);
	
}