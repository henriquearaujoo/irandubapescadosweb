package com.irandubamodulo01.daoimpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.irandubamodulo01.dao.CamaraDAO;
import com.irandubamodulo01.dao.RastreabilidadeDAO;
import com.irandubamodulo01.dao.TipoPeixeDAO;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.Rastreabilidade;
import com.irandubamodulo01.model.TipoPeixe;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.FormatterUtil;
import com.irandubamodulo01.util.PeixePesoUtil;

public class RastreabilidadeDAOImpl extends DAOimpl<Rastreabilidade, Long> implements RastreabilidadeDAO{

	private EntityManager em;
	
	@Inject
	public RastreabilidadeDAOImpl(EntityManager em){
		this.em = em;
	}
	
	public RastreabilidadeDAOImpl(){}
	
	@Override
	public List<Rastreabilidade> filtrados(Filtro filtro) {
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

		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(TipoPeixe.class);

		if (filtro.getDescricao() != null) {
			criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(),
					MatchMode.ANYWHERE));
		}

		return criteria;
	}
	
	@Override
	public List<Rastreabilidade> getRastreabilidadesPorPeriodo(Date dataInicio, Date dataFim){
		String inicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataInicio);
		String fim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataFim);
		String sql = "from Rastreabilidade r where to_char(r.data, 'dd/MM/yyyy HH24:mm:ss') between '" + inicio + "' and '" + fim + "'";
		Query query = em.createQuery(sql);
		return query.getResultList();
	}

	
}
