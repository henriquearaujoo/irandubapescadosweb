package com.irandubamodulo01.daoimpl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.irandubamodulo01.dao.AdiantamentoDAO;
import com.irandubamodulo01.dao.PerfilDAO;
import com.irandubamodulo01.model.AdiantamentoFornecedor;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Perfil;

public class PerfilDAOImpl extends DAOimpl<Perfil, Long> implements PerfilDAO{

	private EntityManager em;
	
	@Inject
	public  PerfilDAOImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public List<Perfil> getPerfis() {
		String jpql = "from Perfil";
		Query query = em.createQuery(jpql);  		
		return query.getResultList();
	}

	
}
