package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Fornecedor;

public interface BarcoDAO extends DAO<Barco, Long>{

	public List<Barco> getBarcosPorFornecedor(Fornecedor fornecedor);
	public List<Barco> getBarcosComplete(Fornecedor fornecedor);
	public Barco getBarcoPorNome(String nome);
	public Barco getBarcoPorNomeEFornecedor(String nome, Fornecedor fornecedor);
}
