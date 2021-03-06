package com.irandubamodulo01.daoimpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;

import com.irandubamodulo01.annotation.Transactional;
import com.irandubamodulo01.dao.PagamentoDAO;
import com.irandubamodulo01.enumerated.StatusCompra;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Pagamento;
import com.irandubamodulo01.model.TipoPagamento;
import com.irandubamodulo01.util.Filtro;


public class PagamentoDAOImpl extends DAOimpl<Pagamento, Long> implements PagamentoDAO{

	
	private EntityManager em;
	
	@Inject
    public PagamentoDAOImpl(EntityManager em) {
	   this.em = em;
	}
	
	@Override
	public List<Pagamento> getPagamentoPorFornecedorEtipo(Fornecedor fornecedor) {
		String jpql = "select new Pagamento(p.id, p.valor) from Pagamento p where p.compra.fornecedor.id = :id and tipoPagamento = :adiantamento " +
						" AND p.compra.status = true AND p.compra.pause = false and p.compra.statusCompra = 'PAGO'";
		Query query = em.createQuery(jpql);  
		query.setParameter("id", fornecedor.getId().longValue());
		query.setParameter("adiantamento", TipoPagamento.ADIANTAMENTO);
		return query.getResultList();
	}
	
	@Override
	public List<Pagamento> getPagamentoPorCompra(Compra compra){
		String jpql = "select p from Pagamento p where p.compra.id = :id";
		Query query = em.createQuery(jpql);  
		query.setParameter("id", compra.getId().longValue());
		return query.getResultList();
	}

	@Override
	public List<Pagamento> getPagamentoPorCompraSemTransferencia(Compra compra){
		String jpql = "select p from Pagamento p where p.compra.id = :id and p.tipoPagamento <> '" + TipoPagamento.TRANSFERENCIA + "'";
		Query query = em.createQuery(jpql);
		query.setParameter("id", compra.getId().longValue());
		return query.getResultList();
	}

	@Override
	public List<Pagamento> getPagamentosPendentesPorCompra(Compra compra){
		String jpql = "select p from Pagamento p where p.compra.id = :id and p.pendente = true and  p.tipoPagamento = '" + TipoPagamento.TRANSFERENCIA + "'";
		Query query = em.createQuery(jpql);
		query.setParameter("id", compra.getId().longValue());
		return query.getResultList();
	}

	@Override
	@Transactional
	public void removePagamentosPorCompra(Compra compra) {
		String jpql = "delete  from Pagamento  where compra.id = :id";
		Query query = em.createQuery(jpql);  
		query.setParameter("id", compra.getId().longValue());
		query.executeUpdate();
	}
	
	@Override
	public int quantidadeFiltrados(Filtro filtro){
		 Criteria criteria = criarCriteria(filtro);
		 criteria.setProjection(Projections.rowCount());
		 return ((Number)criteria.uniqueResult()).intValue();
	}
	
	private Criteria criarCriteria(Filtro filtro){
		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Pagamento.class, "p");
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("p.id").as("id"))
				.add(Projections.property("p.valor").as("valor"))
				.add(Projections.property("p.pendente").as("pendente"))
				.add(Projections.property("p.autorizado").as("autorizado"))
				.add(Projections.property("p.tipoPagamento").as("tipoPagamento"))
				.add(Projections.property("p.dataPagamento").as("dataPagamento"))
				.add(Projections.property("p.compra").as("compra"))
				.add(Projections.property("p.conta").as("conta")));

		criteria.add(Restrictions.eq("tipoPagamento", TipoPagamento.TRANSFERENCIA));
		
		if (filtro.getPendente() != null)
			criteria.add(Restrictions.eq("pendente", filtro.getPendente()));
		
		criteria.createAlias("compra", "c");

		criteria.add(Restrictions.eq("c.status", true));
		criteria.add(Restrictions.eq("c.pause", false));
		criteria.add(Restrictions.eq("c.autorizado", true));
		
		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			criteria.createAlias("compra.fornecedor", "f");
			criteria.add(Restrictions.disjunction());
			criteria.add(Restrictions.ilike("f.nome", filtro.getNome(), MatchMode.ANYWHERE)); 
		}


		
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null){
			criteria.add(Restrictions.between("c.dataCompra", filtro.getDataInicio(), filtro.getDataFinal()));
		}else if (filtro.getDataInicio() != null && filtro.getDataFinal() == null){
			criteria.add(Restrictions.sqlRestriction("to_char(c1_.datacompra, 'dd/MM/yyyy') = '" + new SimpleDateFormat("dd/MM/yyyy").format(filtro.getDataInicio()) + "'"));
		}else if (filtro.getDataInicio() == null && filtro.getDataFinal() != null){
			criteria.add(Restrictions.sqlRestriction("to_char(c1_.datacompra, 'dd/MM/yyyy') = '" + new SimpleDateFormat("dd/MM/yyyy").format(filtro.getDataFinal()) + "'"));
		}
		
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pagamento> filtrados(Filtro filtro){
		
		Criteria criteria =  criarCriteria(filtro);
		
		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());
		
		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		}else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}
		
		criteria.setResultTransformer(new AliasToBeanResultTransformer(Pagamento.class));
		
		return criteria.list();
	}

	@Override
	public Integer obterQtdePagamentosPendentes(){
		String sql = "select count(*) from pagamento p join compra c on c.id = p.compra_id " +
						" where p.pendente = true and p.tipopagamento = 'TRANSFERENCIA' " +
						" and c.status = true and c.pause = false and c.autorizado = true";
		Query query = em.createNativeQuery(sql);
		Object object = query.getSingleResult();
		return object != null ? Integer.parseInt(object.toString()) : 0;
	}

	@Override
	public List<Pagamento> obterDescontosDeAdiantamentoFornecedor(Fornecedor fornecedor){

		String sql = "select row_number() OVER (PARTITION by 0 order by p.datapagamento desc) as seq, c.codigo, " +
				"to_char(c.datacompra, 'dd/MM/yyyy') as datacompra, to_char(p.datapagamento, 'dd/MM/yyyy') as datapagamento, " +
				"p.valor, case when c.status = true and c.statuscompra = 'PAGO' then 'Compra finalizada' " +
				"when c.status = false and c.statuscompra <> 'PAGO' then 'Compra não finalizada' " +
				"when c.status = true and c.statuscompra = 'FINALIZADA_PAGAMENTO_PENDENTE' then 'Pagamento pendente' " +
				"end as status " +
				"from pagamento p " +
				"join compra c on c.id = p.compra_id " +
				"where c.pause = false and p.tipopagamento = 'ADIANTAMENTO' and c.fornecedor_id = :id " +
				"order by p.datapagamento desc";

		Query query = em.createNativeQuery(sql);
		query.setParameter("id", fornecedor.getId().longValue());

		List<Pagamento> pagamentos = new ArrayList<Pagamento>();

		try{
			List<Object[]> objects = query.getResultList();

			for (Object[] obj : objects){
				Pagamento pagamento = new Pagamento();
				pagamento.setSeq(Integer.parseInt(obj[0].toString()));
				pagamento.setCompra(new Compra());
				pagamento.getCompra().setCodigo(obj[1].toString());
				pagamento.getCompra().setDataCompra(new SimpleDateFormat("dd/MM/yyyy").parse(obj[2].toString()));
				pagamento.setDataPagamento(new SimpleDateFormat("dd/MM/yyyy").parse(obj[3].toString()));
				pagamento.setValor(new BigDecimal(obj[4].toString()));
				pagamento.setStatusPagamento(obj[5].toString());

				pagamentos.add(pagamento);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return pagamentos;
	}

	
}
