package com.irandubamodulo01.daoimpl;

import com.irandubamodulo01.dao.FornecedorDAO;
import com.irandubamodulo01.dao.VendedorDAO;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Vendedor;
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
import java.util.List;

public class VendedorDAOImpl extends DAOimpl<Vendedor, Long> implements
		VendedorDAO {

	private EntityManager em;

	@Inject
	public VendedorDAOImpl(EntityManager em){
		this.em = em;
	}

	@Override
	public List<Vendedor> filtrados(Filtro filtro) {
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
		Criteria criteria = session.createCriteria(Vendedor.class);

		if (filtro.getNome() != null) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(),
					MatchMode.START));
		}

		return criteria;
	}

	@Override
	public List<Vendedor> getVendedoresComplete(){
		String jpql = "select new Vendedor(v.id, v.nome) from Vendedor v order by v.nome";
		Query query = em.createQuery(jpql);
		return query.getResultList();
	}

	@Override
	public Vendedor getVendedorPorNome(String nome){
		String jpql = "select new Vendedor(v.id, v.nome, v.porcentagemComissao) from Vendedor v where v.nome = :nome";
		Query query = em.createQuery(jpql);
		query.setParameter("nome", nome);
		return (Vendedor) query.getSingleResult();
	}
}
