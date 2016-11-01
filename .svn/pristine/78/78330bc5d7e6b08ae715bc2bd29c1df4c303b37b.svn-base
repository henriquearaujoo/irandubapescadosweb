package com.irandubamodulo01.daoimpl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.irandubamodulo01.dao.ConfiguracoesDAO;
import com.irandubamodulo01.dao.PeixeDAO;
import com.irandubamodulo01.model.Configuracoes;
import com.irandubamodulo01.model.Lote;
import com.irandubamodulo01.model.Peixe;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.JPAUtil;

public class ConfiguracoesDAOImpl extends DAOimpl<Configuracoes, Long>  implements ConfiguracoesDAO{
	
	private EntityManager em;
	
	@Inject
	public ConfiguracoesDAOImpl(EntityManager em){
		this.em = em;
	}

	@Override
	public List<Configuracoes> getConfiguracoes() {
		String jpql  = "from Configuracoes";
		Query query = em.createQuery(jpql, Configuracoes.class);
		return query.getResultList();
	}
	
	@Override
	public Configuracoes getConfiguracao() {
		try {
			String jpql  = "from Configuracoes";
			Query query = em.createQuery(jpql, Configuracoes.class);
			return (Configuracoes) query.getResultList().get(0);
		} catch (Exception e) {
			return null;
		}
		
	}
	
}