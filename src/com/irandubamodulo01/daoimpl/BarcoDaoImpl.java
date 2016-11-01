package com.irandubamodulo01.daoimpl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.irandubamodulo01.dao.BarcoDAO;
import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Fornecedor;

public class BarcoDaoImpl extends DAOimpl<Barco, Long> implements BarcoDAO{


	private EntityManager em;
	
	@Inject
	public BarcoDaoImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public List<Barco> getBarcosPorFornecedor(Fornecedor fornecedor){
		String jpql = "select b from Barco b where b.fornecedor.id = :id";
		Query query = em.createQuery(jpql);  
		query.setParameter("id", fornecedor.getId().longValue());
		return query.getResultList();
	}

	@Override
	public List<Barco> getBarcosComplete(Fornecedor fornecedor){
		String jpql = "select new Barco(b.id, b.nome) from Barco b where b.fornecedor.id = :id order by b.nome";
		Query query = em.createQuery(jpql);
		query.setParameter("id", fornecedor.getId().longValue());
		return query.getResultList();
	}

	@Override
	public Barco getBarcoPorNome(String nome){
		String jpql = "select new Barco(b.id, b.nome) from Barco b where b.nome = :nome";
		Query query = em.createQuery(jpql);
		query.setParameter("nome", nome);
		return (Barco) query.getSingleResult();
	}

	@Override
	public Barco getBarcoPorNomeEFornecedor(String nome, Fornecedor fornecedor){
		String jpql = "select new Barco(b.id, b.nome) from Barco b where b.nome = :nome and b.fornecedor.id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("nome", nome);
		query.setParameter("id", fornecedor.getId().longValue());
		List<Barco> list = query.getResultList();
		return list.size() > 0 ? list.get(0) : null;
	}
}
