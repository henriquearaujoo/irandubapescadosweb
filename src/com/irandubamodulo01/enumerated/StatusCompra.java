package com.irandubamodulo01.enumerated;

public enum StatusCompra {

	DEFAULT("Todas"),
	AGUARDA_AUTORIZACAO("Aguardando autorização"),
	AUTORIZADO("Autorizada"),
	DESCARTADA("Descartada"),
	ENVIADO("Enviada do primeiro posto"),
	PAGO("Paga"),
	RETORNADO("Retornada para correção"),
	RETORNADO_INICIO("Retornada para o primeiro posto"),
	REABERTA("Compra reaberta"),
	SALVA("Salva não enviada"),
	FINALIZADA_PAGAMENTO_PENDENTE("Pagamento pendente");

	private String status;
	
	private StatusCompra(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
