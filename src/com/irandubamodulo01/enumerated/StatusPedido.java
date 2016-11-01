package com.irandubamodulo01.enumerated;

public enum StatusPedido {

	AGUARDANDO_EMBARQUE("Aguardando embarque"),
	AGUARDANDO_DADOS_TRANSPORTE("Aguardando dados do transporte"),
	ENVIADO_PARA_APROVACAO("Enviado para aprovação"),
	REPROVADO("Reprovado pelo admin"),
	EMBARCADO("Embarcado"),
	RETORNADO("Retorno para correção"),
	FINALIZADO("Finalizado");

	private String status;

	private StatusPedido(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
