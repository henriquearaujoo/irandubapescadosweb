package com.irandubamodulo01.daoimpl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.irandubamodulo01.dao.BarcoDAO;
import com.irandubamodulo01.dao.ContaDAO;
import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Conta;
import com.irandubamodulo01.model.Fornecedor;

public class ContaDaoImpl extends DAOimpl<Conta, Long> implements ContaDAO{


	private EntityManager em;
	
	@Inject
	public ContaDaoImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public List<Conta> getContasPorFornecedor(Fornecedor fornecedor){
		String jpql = "select c from Conta c where c.fornecedor.id = :id";
		Query query = em.createQuery(jpql);  
		query.setParameter("id", fornecedor.getId().longValue());
		return query.getResultList();
	}

}
