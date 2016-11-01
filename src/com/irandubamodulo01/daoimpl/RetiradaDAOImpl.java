package com.irandubamodulo01.daoimpl;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.irandubamodulo01.dao.RetiradaDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import com.irandubamodulo01.enumerated.StatusArmazenamento;
import com.irandubamodulo01.model.Retirada;
import com.irandubamodulo01.util.Filtro;

public class RetiradaDAOImpl extends DAOimpl<Retirada, Long> implements RetiradaDAO{

	private EntityManager em;
	
	@Inject
	public RetiradaDAOImpl(EntityManager em){
		this.em = em;
	}
	
	public RetiradaDAOImpl(){}
	
	
	@Override
	public List<Retirada> filtrados(Filtro filtro) {
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
		Criteria criteria = session.createCriteria(Retirada.class);

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null){
			criteria.add(Restrictions.between("data", filtro.getDataInicio(), filtro.getDataFinal()));
		}else if (filtro.getDataInicio() != null && filtro.getDataFinal() == null){
			criteria.add(Restrictions.sqlRestriction("to_char(data, 'dd/MM/yyyy') = '" + new SimpleDateFormat("dd/MM/yyyy").format(filtro.getDataInicio()) + "'"));
		}else if (filtro.getDataInicio() == null && filtro.getDataFinal() != null){
			criteria.add(Restrictions.sqlRestriction("to_char(data, 'dd/MM/yyyy') = '" + new SimpleDateFormat("dd/MM/yyyy").format(filtro.getDataFinal()) + "'"));
		}
		  
		if (filtro.getFiltroStatus() != null) {
			criteria.add(Restrictions.eq("status", filtro.getFiltroStatus()));
		}
		if (filtro.getUsuario() != null)
		if (!filtro.getUsuario().getPerfil().getDescricao().equals("Admin")) {
			String sql = "status != '"+StatusArmazenamento.RETORNADO+"'";
			criteria.add(Restrictions.sqlRestriction(sql));
		}

		return criteria;
	}

	
}
