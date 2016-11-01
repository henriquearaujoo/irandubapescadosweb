package com.irandubamodulo01.service;

import java.math.BigDecimal;

import com.irandubamodulo01.dao.PagamentoDAO;
import com.irandubamodulo01.model.AdiantamentoFornecedor;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Conta;
import com.irandubamodulo01.model.Pagamento;

public interface ExecutaPagamento {

	public Pagamento executaPagamento(BigDecimal valor, Compra compra, PagamentoDAO dao, Conta conta, String nomeCheque, String numeroCheque, AdiantamentoFornecedor adiantamento);

	public Pagamento alterarPagamento(Pagamento pagamento, PagamentoDAO dao);
	
}
