package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.Embalagem;
import com.irandubamodulo01.util.Filtro;

public interface EmbalagemDAO extends DAO<Embalagem, Long>{

	public int quantidadeFiltrados(Filtro filtro);
	public List<Embalagem> filtrados(Filtro filtro);
	public List<Embalagem> getEmbalagensComplete();
}
