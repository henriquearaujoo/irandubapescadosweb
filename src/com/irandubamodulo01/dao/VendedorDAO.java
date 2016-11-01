package com.irandubamodulo01.dao;

import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Vendedor;
import com.irandubamodulo01.util.Filtro;

import java.util.List;

public interface VendedorDAO extends DAO<Vendedor, Long> {

	public List<Vendedor> filtrados(Filtro filtro);
	public int quantidadeFiltrados(Filtro filtro);
	public List<Vendedor> getVendedoresComplete();
	public Vendedor getVendedorPorNome(String nome);
}
