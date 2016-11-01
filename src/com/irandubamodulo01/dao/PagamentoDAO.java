package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Pagamento;
import com.irandubamodulo01.util.Filtro;

public interface PagamentoDAO extends DAO<Pagamento, Long>{

	public List<Pagamento> getPagamentoPorFornecedorEtipo(Fornecedor fornecedor);
	
	public List<Pagamento> getPagamentoPorCompra(Compra compra);
	
	public void removePagamentosPorCompra(Compra compra);
	
	public List<Pagamento> filtrados(Filtro filtro);
	
	public int quantidadeFiltrados(Filtro filtro);

	public List<Pagamento> getPagamentoPorCompraSemTransferencia(Compra compra);

	public Integer obterQtdePagamentosPendentes();

	public List<Pagamento> obterDescontosDeAdiantamentoFornecedor(Fornecedor fornecedor);

	public List<Pagamento> getPagamentosPendentesPorCompra(Compra compra);
	
}
