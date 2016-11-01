package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Conta;
import com.irandubamodulo01.model.Fornecedor;

public interface ContaDAO extends DAO<Conta, Long>{

	public List<Conta> getContasPorFornecedor(Fornecedor fornecedor);
}
