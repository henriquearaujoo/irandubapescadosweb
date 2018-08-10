package com.irandubamodulo01.daoimpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.irandubamodulo01.util.FormatterUtil;
import com.irandubamodulo01.util.PeixePesoUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;

import com.irandubamodulo01.annotation.Transactional;
import com.irandubamodulo01.dao.CompraDAO;
import com.irandubamodulo01.enumerated.StatusCompra;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Lote;
import com.irandubamodulo01.util.Filtro;


public class CompraDAOImpl extends DAOimpl<Compra, Long> implements CompraDAO {
	
	private EntityManager em;
	
	@Inject
	public CompraDAOImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public int quantidadeFiltrados(Filtro filtro){
		 Criteria criteria = criarCriteria(filtro);
		 criteria.setProjection(Projections.rowCount());
		 return ((Number)criteria.uniqueResult()).intValue();
	}
	
	private Criteria criarCriteria(Filtro filtro){

		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Compra.class);

		if (filtro.getCodigo() != null && !filtro.getCodigo().isEmpty())
			criteria.add(Restrictions.ilike("codigo", filtro.getCodigo(), MatchMode.START));
		
		if (filtro.getStatusCompra() != null && !filtro.getStatusCompra().equals(StatusCompra.DEFAULT))
			criteria.add(Restrictions.eq("statusCompra", filtro.getStatusCompra()));
		
		if (filtro.getStatus() != null)
			criteria.add(Restrictions.eq("status", filtro.getStatus()));
		
		if (filtro.getPause() != null)
			criteria.add(Restrictions.eq("pause", filtro.getPause()));
		
		if (filtro.getAutorizado() != null)
			criteria.add(Restrictions.eq("autorizado", filtro.getAutorizado()));

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			criteria.createAlias("fornecedor", "f");
			criteria.add(Restrictions.disjunction());
			criteria.add(Restrictions.ilike("f.nome", filtro.getNome(), MatchMode.ANYWHERE)); 
		}
		
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null){
			criteria.add(Restrictions.between("dataCompra", filtro.getDataInicio(), filtro.getDataFinal()));
		}else if (filtro.getDataInicio() != null && filtro.getDataFinal() == null){
			criteria.add(Restrictions.sqlRestriction("to_char(datacompra, 'dd/MM/yyyy') = '" + new SimpleDateFormat("dd/MM/yyyy").format(filtro.getDataInicio()) + "'"));
		}else if (filtro.getDataInicio() == null && filtro.getDataFinal() != null){
			criteria.add(Restrictions.sqlRestriction("to_char(datacompra, 'dd/MM/yyyy') = '" + new SimpleDateFormat("dd/MM/yyyy").format(filtro.getDataFinal()) + "'"));
		}
		
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Compra> filtrados(Filtro filtro){
		
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
	
	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Lote> getListaLotesCompra(Compra compra){
		
		//em = JPAUtil.getEntityManager();
		
		List<Lote> lotes = new ArrayList<Lote>();
		
		try {

			//em.getTransaction().begin();
			String jpql = "from Lote where compra.id = :idCompra";
			Query query = em.createQuery(jpql);
			query.setParameter("idCompra", compra.getId());
			lotes = query.getResultList();
			
			//em.getTransaction().commit();
		} catch (Exception e) {
			//em.getTransaction().rollback();
		}finally{
			//em.close();
		}
		
		return lotes;
	}

	@Override
	public Integer obterQtdeComprasAPagar(){
		String sql = "select count(*) from compra where status = false and pause = false";
		Query query = em.createNativeQuery(sql);
		Object object = query.getSingleResult();
		return object != null ? Integer.parseInt(object.toString()) : 0;
	}

	@Override
	public Integer obterQtdeComprasAAutorizar(){
		String sql = "select count(*) from compra where status = false and pause = false and autorizado = false and statuscompra = 'AGUARDA_AUTORIZACAO'";
		Query query = em.createNativeQuery(sql);
		Object object = query.getSingleResult();
		return object != null ? Integer.parseInt(object.toString()) : 0;
	}

	@Override
	public BigDecimal obterValorTotalComprasPorData(Date date){
		String data = new SimpleDateFormat("dd/MM/yyyy").format(date);
		String sql = "select sum(c.valortotal) from compra c where c.status = true AND c.pause = false and c.statuscompra = 'PAGO' and to_char(c.datacompra, 'dd/MM/yyyy') = '" + data + "'";
		Query query = em.createNativeQuery(sql);
		Object object = query.getSingleResult();
		return object != null ? new BigDecimal(object.toString()) : BigDecimal.ZERO;
	}

	@Override
	public BigDecimal obterValorTotalComprasPorPeriodo(Date dataInicio, Date dataFim){
		String inicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataInicio);
		String fim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataFim);
		String sql = "select sum(c.valortotal) from compra c where c.status = true AND c.pause = false and c.statuscompra = 'PAGO' and to_char(c.datacompra, 'dd/MM/yyyy HH24:mm:ss') between '" + inicio + "' and '" + fim + "'";
		Query query = em.createNativeQuery(sql);
		Object object = query.getSingleResult();
		return object != null ? new BigDecimal(object.toString()) : BigDecimal.ZERO;
	}

	@Override
	public BigDecimal obterPesoTotalComprasPorData(Date date){
		String data = new SimpleDateFormat("dd/MM/yyyy").format(date);
		String sql = "select sum(l.peso - (l.pesocacapa * l.qtdcaixas)) from lote l " +
					 " join compra c on c.id = l.compra_id " +
					 " where c.status = true AND c.pause = false and c.statuscompra = 'PAGO'  and to_char(c.datacompra, 'dd/MM/yyyy') = '" + data + "'";
		Query query = em.createNativeQuery(sql);
		Object object = query.getSingleResult();
		return object != null ? new BigDecimal(object.toString()) : BigDecimal.ZERO;
	}

	@Override
	public BigDecimal obterPesoTotalComprasNaoFinalizadasPorData(Date date){
		String data = new SimpleDateFormat("dd/MM/yyyy").format(date);
		String sql = "select sum(l.peso - (l.pesocacapa * l.qtdcaixas)) from lote l " +
				" join compra c on c.id = l.compra_id " +
				" where to_char(c.datacompra, 'dd/MM/yyyy') = '" + data + "'";
		Query query = em.createNativeQuery(sql);
		Object object = query.getSingleResult();
		return object != null ? new BigDecimal(object.toString()) : BigDecimal.ZERO;
	}

	@Override
	public BigDecimal obterPesoTotalComprasPorPeriodo(Date dataInicio, Date dataFim){
		String inicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataInicio);
		String fim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataFim);
		String sql = "select sum(l.peso - (l.pesocacapa * l.qtdcaixas)) from lote l " +
					 " join compra c on c.id = l.compra_id " +
				     " where c.status = true AND c.pause = false and c.statuscompra = 'PAGO' and to_char(c.datacompra, 'dd/MM/yyyy HH24:mm:ss') between '" + inicio + "' and '" + fim + "'";
		Query query = em.createNativeQuery(sql);
		Object object = query.getSingleResult();
		return object != null ? new BigDecimal(object.toString()) : BigDecimal.ZERO;
	}

	@Override
	public List<PeixePesoUtil> obterTop10TotalPeixesPesadosPorData(Date date){
		List<PeixePesoUtil> peixes = new ArrayList<PeixePesoUtil>();
		String data = new SimpleDateFormat("dd/MM/yyyy").format(date);
		String sql = "select p.descricao, sum(l.peso - (l.pesocacapa * l.qtdcaixas)) as peso " +
						" from lote l " +
						" join compra c on c.id = l.compra_id  " +
						" join peixe p on p.id = l.peixe_id " +
						" where to_char(c.datacompra, 'dd/MM/yyyy') = '" + data + "'" +
						" group by p.descricao " +
						" order by peso desc " +
						" limit 5";
		Query query = em.createNativeQuery(sql);
		List<Object[]> list = query.getResultList();
		for(Object[] obj : list){
			PeixePesoUtil p = new PeixePesoUtil();
			p.setDescricao(obj[0].toString());
			p.setPeso(FormatterUtil.getValorFormatado(new BigDecimal(obj[1].toString())));
			peixes.add(p);
		}
		return peixes;
	}
}
