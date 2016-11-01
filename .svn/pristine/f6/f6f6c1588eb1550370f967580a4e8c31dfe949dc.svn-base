package com.irandubamodulo01.service;

import java.math.BigDecimal;

import com.irandubamodulo01.dao.PagamentoDAO;
import com.irandubamodulo01.model.*;

public class PagamentoTransferencia implements ExecutaPagamento{

	@Override
	public Pagamento executaPagamento(BigDecimal valor, Compra compra, PagamentoDAO dao, Conta conta, String nomeCheque, String numeroCheque, AdiantamentoFornecedor adiantamento) {
	
		if (valor == null) {
			return null;
		}
		
		if (valor.compareTo(BigDecimal.ZERO) == 0) {
			return null;
			
		}
		
		Pagamento pg = new Pagamento();
		pg.setPendente(true);
		pg.setConta(conta);
		pg.setAutorizado(true);
		pg.setValor(valor);
		pg.setCompra(compra);
		pg.setTipoPagamento(TipoPagamento.TRANSFERENCIA);
		pg.setAdiantamento(null);
		pg = dao.save(pg);

		return pg;
	}

	@Override
	public Pagamento alterarPagamento(Pagamento pagamento, PagamentoDAO dao) {
		return dao.save(pagamento);
	}

}
