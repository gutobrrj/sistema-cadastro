package br.com.cnsoftware.sistema.controller.dto;

//Apenas atributos que o frontend espera. Valor definido no arquivo (login-response.type.ts) no front
public record ResponseDTO(String nome, String token) {}
