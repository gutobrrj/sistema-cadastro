package br.com.cnsoftware.sistema.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cnsoftware.sistema.exception.RegraNegocioException;
import br.com.cnsoftware.sistema.model.entity.Cliente;
import br.com.cnsoftware.sistema.repository.ClienteRepository;

@Service
@Transactional
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	public Cliente salvar(Cliente cliente) {
		
		boolean existeCpfCnpj = false;
		Optional<Cliente> clienteRetornado = repository.findByCpfCnpj(cliente.getCpfCnpj());
		
		if(clienteRetornado.isPresent()) {
			if(!clienteRetornado.get().getId().equals(cliente.getId())) {
				existeCpfCnpj = true;
			}
		}
		
		if(existeCpfCnpj) {
			System.out.println("1");
			throw new RegraNegocioException("CPF/CNPJ já cadastrado no  sistema.");
		}
		
		return repository.save(cliente);
	}
	
	public List<Cliente> listarTodos() {
		return repository.findAll();
	}
	
	public Optional<Cliente> buscarPorId(Long id) {
		return repository.findById(id);
	}
	
	public void remover(Long id) {
		repository.deleteById(id);
	}
}
