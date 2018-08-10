package com.irandubamodulo01.daoimpl;

import java.math.BigDecimal;
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

import com.irandubamodulo01.annotation.Transactional;
import com.irandubamodulo01.dao.LoteDAO;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Lote;
import com.irandubamodulo01.model.Rastreabilidade;
import com.irandubamodulo01.model.TipoPeixe;
import com.irandubamodulo01.util.Filtro;


public class LoteDAOImpl extends DAOimpl<Lote, Long> implements LoteDAO{
	
	
	private EntityManager em;
	
	@Inject
	public LoteDAOImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public List<Lote> filtrados(Filtro filtro) {
		Criteria criteria = criarCriteria(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		} else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}
		
		if (filtro.getCompra() != null) {
			criteria.createAlias("compra", "c");
			criteria.add(Restrictions.eq("c.id", filtro.getCompra().getId()));
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

		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Lote.class);

		if (filtro.getDescricao() != null) {
			criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(),
					MatchMode.ANYWHERE));
		}

		return criteria;
	}
	
	@Transactional
	@Override
	public void salvarLotes(List<Lote> lotes){
		//em = getEntityManager();			
		for (Lote lote : lotes) {
			em.persist(lote);
		}

	}

	@Override
	public List<Lote> buscarLotePorCompra(Compra compra) {
		String jpql = "select l from Lote l where l.compra.id = :id";
		Query query =  em.createQuery(jpql);
		query.setParameter("id", compra.getId());
		return query.getResultList();
	}
	
	@Override
	public List<Lote> buscarLotePorCompraa(Compra compra) {
		String jpql = "select new Lote(l.id, l.valorUnitarioPeixe, l.peso, l.qtdCaixas, l.pesoCacapa, p.id, p.descricao, p.valor, l.valor, l.acrescimo, l.desconto, l.isPrecoDiferenciado) "
				+ "from Lote l left join l.peixe as p where l.compra.id = :id order by p.descricao";
		Query query =  em.createQuery(jpql);
		query.setParameter("id", compra.getId());
		return query.getResultList();
	}
	

}
