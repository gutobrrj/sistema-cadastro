package br.com.sistema.controller.dto;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import br.com.sistema.model.entity.Contato;
import br.com.sistema.model.entity.Endereco;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record ClienteRequestDTO2(
	Long id,
	@NotNull String nome,
	@NotNull @Past LocalDate dataNascimento,
	@NotNull String tipoPessoa,
	@NotNull String cpfCNPJ,
	@NotNull String rg,
	@Length String login,
	@Length String senha,
	@NotNull String genero,
	@NotNull List<Endereco> enderecos,
	@NotNull List<Contato> contatos
) {}
