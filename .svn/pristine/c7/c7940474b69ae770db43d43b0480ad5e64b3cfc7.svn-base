package com.irandubamodulo01.dao;

import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Peixe;
import com.irandubamodulo01.model.PrecoDiferenciado;

import java.util.List;

public interface PrecoDiferenciadoDAO extends DAO<PrecoDiferenciado, Long>{

	public List<PrecoDiferenciado> getPrecoDiferenciadoPorFornecedor(Fornecedor fornecedor);

	public PrecoDiferenciado getPrecoDiferenciadoPorFornecedorEPeixe(Fornecedor fornecedor, Peixe peixe);

	public List<PrecoDiferenciado> getPrecoDiferenciadoPorFornecedorPeixeDif(Fornecedor fornecedor, List<Long> idsPeixes);
}
