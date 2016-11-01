package com.irandubamodulo01.dao;

import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Transportadora;
import com.irandubamodulo01.util.Filtro;

import java.util.List;

public interface TransportadoraDAO extends DAO<Transportadora, Long> {

	public List<Transportadora> filtrados(Filtro filtro);
	public int quantidadeFiltrados(Filtro filtro);
	public List<Transportadora> getTransportadorasComplete();
	public Transportadora getTransportadoraPorNome(String nome);

}
