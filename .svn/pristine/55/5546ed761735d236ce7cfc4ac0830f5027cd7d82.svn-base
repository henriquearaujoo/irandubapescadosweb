package com.irandubamodulo01.dao;

import com.irandubamodulo01.model.Cliente;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.util.Filtro;

import java.util.List;

public interface ClienteDAO extends DAO<Cliente, Long> {

	public List<Cliente> filtrados(Filtro filtro);
	public int quantidadeFiltrados(Filtro filtro);
	public List<Cliente> getClientesComplete();
	public Cliente getClientePorNome(String nome);

}
