package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.AdiantamentoFornecedor;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Perfil;

public interface PerfilDAO extends DAO<Perfil, Long>{

	public List<Perfil> getPerfis();
	
}
