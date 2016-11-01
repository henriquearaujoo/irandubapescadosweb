package com.irandubamodulo01.dao;


import java.util.List;

import com.irandubamodulo01.model.TamanhoPeixe;
import com.irandubamodulo01.util.Filtro;

public interface TamanhoPeixeDAO extends DAO<TamanhoPeixe, Long>{

	public List<TamanhoPeixe> filtrados(Filtro filtro);
	public int quantidadeFiltrados(Filtro filtro);
	public List<TamanhoPeixe> getTamanhosComplete();
	
}
