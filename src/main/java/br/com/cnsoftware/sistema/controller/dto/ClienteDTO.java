package br.com.cnsoftware.sistema.controller.dto;

import java.time.LocalDate;
import java.util.List;
import br.com.cnsoftware.sistema.model.entity.Endereco;
import br.com.cnsoftware.sistema.model.entity.enums.Sexo;

public record ClienteDTO(
	Long id,
	String nome,
	LocalDate dataNascimento,
	String cpfCNPJ,
	String rg,
	Sexo sexo,
	List<Endereco> enderecos,
	List<String> telefones,
	List<String> emails,
	String login,
	String senha
) {}