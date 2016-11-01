package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.util.Filtro;

public interface CamaraDAO extends DAO<Camara, Long>{

	public List<Camara> filtrados(Filtro filtro);
	public int quantidadeFiltrados(Filtro filtro);
	public List<Camara> getCamarasComplete();
}
