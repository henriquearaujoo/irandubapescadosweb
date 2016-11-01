package com.irandubamodulo01.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.util.Filtro;


public class FornecedorRepository {

	private EntityManager manager;
	
	public FornecedorRepository(EntityManager manager){
		this.manager = manager;
	}
	
	public List<Fornecedor> filtrados(Filtro filtro) {
		Criteria criteria = criarCriteria(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		} else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}

		return criteria.list();
	}

	public int quantidadeFiltrados(Filtro filtro) {
		 Criteria criteria = criarCriteria(filtro);
		 criteria.setProjection(Projections.rowCount());
		 return ((Number)criteria.uniqueResult()).intValue();
	}
	
	private Criteria criarCriteria(Filtro filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Fornecedor.class);

		if (filtro.getNome() != null) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(),
					MatchMode.ANYWHERE));
		}

		return criteria;
	}

	public void salvar(Fornecedor fornec){
		manager.merge(fornec);
	}
	
}
