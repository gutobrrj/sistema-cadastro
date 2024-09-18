package br.com.cnsoftware.sistema.model.entity.enums;

public enum TipoPessoa {

	FISICA("fisica"),
	JURIDICA("juridica");
	
	public String descricao;
	
	private TipoPessoa(String descricao) {
		this.descricao = descricao;
	}
	
}
