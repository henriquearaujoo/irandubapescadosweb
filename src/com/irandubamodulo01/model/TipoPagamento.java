package com.irandubamodulo01.model;

import com.irandubamodulo01.service.ExecutaPagamento;
import com.irandubamodulo01.service.PagamentoAdiantamento;
import com.irandubamodulo01.service.PagamentoCheque;
import com.irandubamodulo01.service.PagamentoDinheiro;
import com.irandubamodulo01.service.PagamentoTransferencia;

public enum TipoPagamento {
	
	DINHEIRO(new PagamentoDinheiro()),
	ADIANTAMENTO(new PagamentoAdiantamento()),
	TRANSFERENCIA(new PagamentoTransferencia()),
	CHEQUE(new PagamentoCheque());

	private final ExecutaPagamento executaPagamento;
	
	
	private TipoPagamento(ExecutaPagamento executaPagamento) {
		this.executaPagamento = executaPagamento;
	}

	public ExecutaPagamento getExecutaPagamento() {
		return executaPagamento;
	}

}
