package com.irandubamodulo01.daoimpl;

import com.irandubamodulo01.dao.ClienteDAO;
import com.irandubamodulo01.dao.FornecedorDAO;
import com.irandubamodulo01.model.Cliente;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Fornecedor;
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

public class ClienteDAOImpl extends DAOimpl<Cliente, Long> implements
		ClienteDAO {

	private EntityManager em;

	@Inject
	public ClienteDAOImpl(EntityManager em){
		this.em = em;
	}

	@Override
	public List<Cliente> filtrados(Filtro filtro) {
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
		Criteria criteria = session.createCriteria(Cliente.class);

		if (filtro.getNome() != null) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(),
					MatchMode.START));
		}

		return criteria;
	}

	@Override
	public List<Cliente> getClientesComplete(){
		String jpql = "select new Cliente(c.id, c.nome) from Cliente c order by c.nome";
		Query query = em.createQuery(jpql);
		return query.getResultList();
	}

	@Override
	public Cliente getClientePorNome(String nome){
		String jpql = "select new Cliente(c.id, c.nome) from Cliente c where c.nome = :nome";
		Query query = em.createQuery(jpql);
		query.setParameter("nome", nome);
		return (Cliente) query.getSingleResult();
	}

}
