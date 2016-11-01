package com.irandubamodulo01.daoimpl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.irandubamodulo01.dao.FornecedorDAO;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.JPAUtil;

public class FornecedorDAOImpl extends DAOimpl<Fornecedor, Long> implements
		FornecedorDAO {

	private EntityManager em;
	
	@Inject
	public FornecedorDAOImpl(EntityManager em){
		this.em = em;
	}

	@Override
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

	@Override
	public int quantidadeFiltrados(Filtro filtro) {
		 Criteria criteria = criarCriteria(filtro);
		 criteria.setProjection(Projections.rowCount());
		 return ((Number)criteria.uniqueResult()).intValue();
	}

	private Criteria criarCriteria(Filtro filtro) {
		//	em = JPAUtil.getEntityManager();
		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Fornecedor.class);

		if (filtro.getNome() != null) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(),
					MatchMode.START));
		}

		return criteria;
	}

	@Override
	public Boolean verificaExisteCompra(Fornecedor fornec){
		//em = JPAUtil.getEntityManager();
		String jpql = "select * from  compra where fornecedor_id  = :param";
		Query query = em.createNativeQuery(jpql, Compra.class);
		query.setParameter("param", fornec.getId().longValue());
		return query.getResultList().size() > 0 ? true : false;
	}

	@Override
	public List<Fornecedor> getFornecedoresComplete(){
		String jpql = "select new Fornecedor(f.id, f.nome) from Fornecedor f order by f.nome";
		Query query = em.createQuery(jpql);
		return query.getResultList();
	}

	@Override
	public Fornecedor getFornecedorPorNome(String nome){
		String jpql = "select new Fornecedor(f.id, f.nome) from Fornecedor f where f.nome = :nome";
		Query query = em.createQuery(jpql);
		query.setParameter("nome", nome);
		return (Fornecedor) query.getSingleResult();
	}

}
