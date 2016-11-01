package com.irandubamodulo01.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.irandubamodulo01.dao.PagamentoDAO;
import com.irandubamodulo01.model.*;

public class PagamentoAdiantamento implements ExecutaPagamento, Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public Pagamento executaPagamento(BigDecimal valor, Compra compra, PagamentoDAO dao, Conta conta, String nomeCheque, String numeroCheque, AdiantamentoFornecedor adiantamento) {

		if (valor == null) {
			return null;
		}

		if (valor.compareTo(BigDecimal.ZERO) == 0) {
			return null;

		}

		Pagamento pg = new Pagamento();
		pg.setPendente(false);
		pg.setAutorizado(true);
		pg.setCompra(compra);
		pg.setDataPagamento(new Date());
		pg.setTipoPagamento(TipoPagamento.ADIANTAMENTO);
		pg.setValor(valor);
		if (adiantamento != null)
			pg.setAdiantamento(adiantamento);
		pg = dao.save(pg); 
		return pg;
	}

	@Override
	public Pagamento alterarPagamento(Pagamento pagamento, PagamentoDAO dao) {
		return dao.save(pagamento);
	}

}
