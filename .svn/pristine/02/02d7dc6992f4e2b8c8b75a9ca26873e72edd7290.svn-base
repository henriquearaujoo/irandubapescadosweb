package com.irandubamodulo01.daoimpl;

import com.irandubamodulo01.dao.ObservacaoArmazenamentoDAO;
import com.irandubamodulo01.model.Armazenamento;
import com.irandubamodulo01.model.Observacao;
import com.irandubamodulo01.model.ObservacaoArmazenamento;
import com.irandubamodulo01.model.Retirada;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ObservacaoArmazenamentoDaoImpl extends DAOimpl<ObservacaoArmazenamento, Long> implements ObservacaoArmazenamentoDAO{

	private EntityManager em;

	public ObservacaoArmazenamentoDaoImpl() {

	}

	@Inject
	public ObservacaoArmazenamentoDaoImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public List<Observacao> getObservacaoByArmazenamento(Armazenamento armazenamento){
		String jpql = "from ObservacaoArmazenamento where armazenamento.id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", armazenamento.getId().longValue());
		return query.getResultList();
		
	}

	@Override
	public List<Observacao> getObservacaoByRetirada(Retirada retirada) {
		String jpql = "from ObservacaoArmazenamento where retirada.id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", retirada.getId().longValue());
		return query.getResultList();
	}
}
