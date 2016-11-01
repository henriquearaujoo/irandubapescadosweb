package com.irandubamodulo01.daoimpl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.irandubamodulo01.dao.UsuarioDAO;
import com.irandubamodulo01.model.Usuario;
import com.irandubamodulo01.util.Filtro;

public class UsuarioDAOImpl extends DAOimpl<Usuario, Long> implements UsuarioDAO{
	
	
	private EntityManager em;
	
	@Inject
	public UsuarioDAOImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public Usuario logar(String login, String senha){
		String jpql = "select u from Usuario u where u.login = :login and u.senha = :senha";
		//em = JPAUtil.getEntityManager();
		Query query = em.createQuery(jpql);  
		query.setParameter("login", login);
		query.setParameter("senha", senha);
		return (Usuario) (query.getResultList().size() > 0 ? query.getResultList().get(0) : null);
				
		
	}

	@Override
	public List<Usuario> filtrar(String s) {
		try {
			String jpql = "from Usuario where upper(nome) like upper(:param)";
			//em = JPAUtil.getEntityManager();
			TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
			query.setParameter("param", "%"+s+"%");
			return query.getResultList().size() > 0 ? query.getResultList() : null;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> filtrados(Filtro filtro){
		
		Criteria criteria =  criarCriteria(filtro);
		
		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());
		
		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		}else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}
		
		
		
		return criteria.list();
	}
	
	@Override
	public int quantidadeFiltrados(Filtro filtro){
		 Criteria criteria = criarCriteria(filtro);
		 criteria.setProjection(Projections.rowCount());
		 return ((Number)criteria.uniqueResult()).intValue();
	}
	
	private Criteria criarCriteria(Filtro filtro){
		//em = JPAUtil.getEntityManager();
		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class);
		
		if (filtro.getNome() != null) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}
		
		return criteria;
	}

}
