package br.com.cnsoftware.sistema.model.entity.enums;

public enum RoleUsuario {

	ADMIN("admin"), USUARIO("user");

	public String descricao;

	private RoleUsuario(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
