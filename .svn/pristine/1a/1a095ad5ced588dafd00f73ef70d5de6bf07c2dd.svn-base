package com.irandubamodulo01.daoimpl;

import com.irandubamodulo01.dao.ClienteDAO;
import com.irandubamodulo01.dao.TransportadoraDAO;
import com.irandubamodulo01.model.Cliente;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Transportadora;
import com.irandubamodulo01.util.Filtro;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class TransportadoraDAOImpl extends DAOimpl<Transportadora, Long> implements
		TransportadoraDAO {

	private EntityManager em;

	@Inject
	public TransportadoraDAOImpl(EntityManager em){
		this.em = em;
	}

	@Override
	public List<Transportadora> filtrados(Filtro filtro) {
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
		Criteria criteria = session.createCriteria(Transportadora.class);

		if (filtro.getNome() != null) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(),
					MatchMode.START));
		}

		return criteria;
	}

	@Override
	public List<Transportadora> getTransportadorasComplete(){
		String jpql = "select new Transportadora(t.id, t.nome) from Transportadora t order by t.nome";
		Query query = em.createQuery(jpql);
		return query.getResultList();
	}

	@Override
	public Transportadora getTransportadoraPorNome(String nome){
		String jpql = "select new Transportadora(t.id, t.nome) from Transportadora t where t.nome = :nome";
		Query query = em.createQuery(jpql);
		query.setParameter("nome", nome);
		return (Transportadora) query.getSingleResult();
	}

}
