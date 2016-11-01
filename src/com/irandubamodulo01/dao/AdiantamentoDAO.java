package com.irandubamodulo01.dao;

import java.util.List;
import com.irandubamodulo01.model.AdiantamentoFornecedor;
import com.irandubamodulo01.model.Fornecedor;

public interface AdiantamentoDAO extends DAO<AdiantamentoFornecedor, Long>{

	public List<AdiantamentoFornecedor> getAdiantamentoPorFornecedor(Fornecedor fornecedor);

	public List<AdiantamentoFornecedor> getChequesDisponiveisAdiantamento(Fornecedor fornecedor);

	public List<AdiantamentoFornecedor> getContasDisponiveisAdiantamento(Fornecedor fornecedor);
	
}
