package com.irandubamodulo01.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.irandubamodulo01.annotation.Transactional;
import com.irandubamodulo01.dao.PagamentoDAO;
import com.irandubamodulo01.model.*;

public class PagamentoCheque implements ExecutaPagamento{

	@Inject
	private EntityManager manager;
	
	@Override
	@Transactional
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
		pg.setValor(valor);
		pg.setCompra(compra);
		pg.setDataPagamento(new Date());
		pg.setTipoPagamento(TipoPagamento.CHEQUE);
		pg.setNomeCheque(nomeCheque);
		pg.setNumeroCheque(numeroCheque);
		pg.setAdiantamento(null);
		pg = dao.save(pg);
		return pg;
	}

	@Override
	public Pagamento alterarPagamento(Pagamento pagamento, PagamentoDAO dao) {
		return dao.save(pagamento);
	}

}
