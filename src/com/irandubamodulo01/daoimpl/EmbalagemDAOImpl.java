package com.irandubamodulo01.daoimpl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.irandubamodulo01.model.TamanhoPeixe;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.irandubamodulo01.dao.CamaraDAO;
import com.irandubamodulo01.dao.EmbalagemDAO;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.Embalagem;
import com.irandubamodulo01.model.TipoPeixe;
import com.irandubamodulo01.util.Filtro;

public class EmbalagemDAOImpl extends DAOimpl<Embalagem, Long> implements EmbalagemDAO{

	private EntityManager em;
	
	@Inject
	public EmbalagemDAOImpl(EntityManager em){
		this.em = em;
	}
	
	public EmbalagemDAOImpl(){}
	
	@Override
	public List<Embalagem> filtrados(Filtro filtro) {
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

	@Override
	public int quantidadeFiltrados(Filtro filtro) {
		 Criteria criteria = criarCriteria(filtro);
		 criteria.setProjection(Projections.rowCount());
		 return ((Number)criteria.uniqueResult()).intValue();
	}

	private Criteria criarCriteria(Filtro filtro) {
		//	em = JPAUtil.getEntityManager();
		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Embalagem.class);

		if (filtro.getDescricao() != null) {
			criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(),
					MatchMode.ANYWHERE));
		}

		return criteria;
	}

	@Override
	public List<Embalagem> getEmbalagensComplete(){
		String jpql = "select new Embalagem(p.id, p.descricao) from Embalagem p order by p.descricao";
		Query query = em.createQuery(jpql);
		return query.getResultList();
	}
}
