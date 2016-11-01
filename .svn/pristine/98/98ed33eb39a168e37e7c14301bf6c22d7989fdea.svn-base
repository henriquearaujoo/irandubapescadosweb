package com.irandubamodulo01.daoimpl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.irandubamodulo01.dao.ObservacaoDAO;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Observacao;

public class ObservacaoDaoImpl extends DAOimpl<Observacao, Long> implements ObservacaoDAO{

	private EntityManager em;
	
	public ObservacaoDaoImpl() {
		
	}
	
	@Inject
	public ObservacaoDaoImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public List<Observacao> getObservacaoPorCompra(Compra compra){
		String jpql = "from Observacao where compra.id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", compra.getId().longValue());
		return query.getResultList();
		
	}
}
