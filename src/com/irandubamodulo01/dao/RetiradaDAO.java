package com.irandubamodulo01.dao;


import com.irandubamodulo01.model.Retirada;
import com.irandubamodulo01.util.Filtro;

import java.util.List;

public interface RetiradaDAO extends DAO<Retirada, Long>{

	public List<Retirada> filtrados(Filtro filtro);
	public int quantidadeFiltrados(Filtro filtro);
	public Retirada obterRetiradaPorId(Long id);
}
