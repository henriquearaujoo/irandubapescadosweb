package com.irandubamodulo01.enumerated;

public enum TipoPessoa {

	FISICA("Física"),
	JURIDICA("Jurídica");

	private String tipo;

	private TipoPessoa(String tipo){
		this.tipo = tipo;
	}
	
	public String getTipo(){
		return tipo;
	}
}
