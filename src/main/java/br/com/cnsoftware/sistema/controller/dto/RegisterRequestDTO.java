package br.com.cnsoftware.sistema.controller.dto;

// DTO criado com base nos dados que o frontEnd(Angular) envia do formulário
public record RegisterRequestDTO(
		String nome, 
		String email, 
		String login, 
		String senha
	) {
}
