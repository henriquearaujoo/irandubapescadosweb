package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Usuario;
import com.irandubamodulo01.util.Filtro;

public interface FornecedorDAO extends DAO<Fornecedor, Long> {

	public List<Fornecedor> filtrados(Filtro filtro);
	public int quantidadeFiltrados(Filtro filtro);
	public Boolean verificaExisteCompra(Fornecedor fornec);
	public List<Fornecedor> getFornecedoresComplete();
	public Fornecedor getFornecedorPorNome(String nome);

}
