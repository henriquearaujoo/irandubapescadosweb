package com.irandubamodulo01.enumerated;

public enum TipoDestinoRetirada {

	PROCESSO("Processo"),
	DESCARTE("Descarte"),
	COZINHA("Cozinha"),
	VENDA("Venda");
	
	private String tipo;
	
	private TipoDestinoRetirada(String tipo){
		this.tipo = tipo;
	}
	
	public String getTipo(){
		return tipo;
	}
}
