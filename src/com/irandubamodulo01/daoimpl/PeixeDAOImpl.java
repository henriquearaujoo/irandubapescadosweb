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

import com.irandubamodulo01.dao.PeixeDAO;
import com.irandubamodulo01.model.Lote;
import com.irandubamodulo01.model.Peixe;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.JPAUtil;

public class PeixeDAOImpl extends DAOimpl<Peixe, Long>  implements PeixeDAO{
	
	private EntityManager em;
	
	@Inject
	public PeixeDAOImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public List<Peixe> filtrar(String s) {
		try {
			String jpql = "from Peixe where upper(descricao) like upper(:param)";
			//em = JPAUtil.getEntityManager();
			TypedQuery<Peixe> query = em.createQuery(jpql, Peixe.class);
			query.setParameter("param", "%"+s+"%");
			return query.getResultList().size() > 0 ? query.getResultList() : null;
		} catch (Exception e) {
			return null;
		}
	}
	 
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Peixe> filtrados(Filtro filtro){
		
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
		Criteria criteria = session.createCriteria(Peixe.class);
		
		if (filtro.getDescricao() != null) {
			criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
		}
	return criteria;
	}


	@Override
	public Boolean verificarVendasEmProduto(Peixe peixe) {
		//em = JPAUtil.getEntityManager();
		String jpql  = "select * from lote where peixe_id = :param";
		Query query = em.createNativeQuery(jpql, Lote.class);
		query.setParameter("param", peixe.getId().longValue());
		return query.getResultList().size() > 0 ? true : false;
	}

	@Override
	public List<Peixe> getPeixesComplete(){
		String jpql = "select new Peixe(p.id, p.descricao) from Peixe p order by p.descricao";
		Query query = em.createQuery(jpql);
		return query.getResultList();
	}

	@Override
	public List<Peixe> getPeixesCompleteVenda(){
		String jpql = "select new Peixe(p.id, p.descricao) from Peixe p where tipoCadastro = 'VENDA' order by p.descricao";
		Query query = em.createQuery(jpql);
		return query.getResultList();
	}

	@Override
	public Peixe getPeixePorDescricao(String descricao){
		String jpql = "select new Peixe(p.id, p.descricao, p.valor) from Peixe p where p.descricao = :descricao";
		Query query = em.createQuery(jpql);
		query.setParameter("descricao", descricao);
		List<Peixe> list = query.getResultList();
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	
	
}