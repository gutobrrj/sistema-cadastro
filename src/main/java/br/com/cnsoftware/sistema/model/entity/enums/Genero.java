package br.com.cnsoftware.sistema.model.entity.enums;

public enum Genero {
	
	FEMININO("feminino"),
	MASCULINO("masculino");
	
    public String descricao;

    private Genero(String descricao)	{
        this.descricao = descricao;
    }        
	
}
