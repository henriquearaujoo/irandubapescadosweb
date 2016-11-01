package com.irandubamodulo01.model;

public enum TipoConta {

	CORRENTE("Corrente"),
	POUPANCA("Poupança");
	
	private final String label;
	
	private TipoConta(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return this.label;
	}
}
