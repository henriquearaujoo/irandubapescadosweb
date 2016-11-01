package com.irandubamodulo01.daoimpl;

import com.irandubamodulo01.dao.BarcoDAO;
import com.irandubamodulo01.dao.PedidoDAO;
import com.irandubamodulo01.enumerated.StatusPedido;
import com.irandubamodulo01.model.*;
import com.irandubamodulo01.util.Filtro;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.List;

public class PedidoDaoImpl extends DAOimpl<Pedido, Long> implements PedidoDAO{


	private EntityManager em;

	@Inject
	public PedidoDaoImpl(EntityManager em){
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
		Criteria criteria = session.createCriteria(Pedido.class, "p");
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("p.id").as("id"))
				.add(Projections.property("p.valor").as("valor"))
				.add(Projections.property("p.data").as("data"))
				.add(Projections.property("p.codigo").as("codigo"))
				.add(Projections.property("p.qtdeParcelas").as("qtdeParcelas"))
				.add(Projections.property("p.status").as("status"))
				.add(Projections.property("p.vendedor").as("vendedor"))
				.add(Projections.property("p.cliente").as("cliente"))
				.add(Projections.property("p.transportadora").as("transportadora")));

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null){
			criteria.add(Restrictions.between("data", filtro.getDataInicio(), filtro.getDataFinal()));
		}else if (filtro.getDataInicio() != null && filtro.getDataFinal() == null){
			criteria.add(Restrictions.sqlRestriction("to_char(data, 'dd/MM/yyyy') = '" + new SimpleDateFormat("dd/MM/yyyy").format(filtro.getDataInicio()) + "'"));
		}else if (filtro.getDataInicio() == null && filtro.getDataFinal() != null){
			criteria.add(Restrictions.sqlRestriction("to_char(data, 'dd/MM/yyyy') = '" + new SimpleDateFormat("dd/MM/yyyy").format(filtro.getDataFinal()) + "'"));
		}

		if (filtro.getStatusPedido() != null) {
			criteria.add(Restrictions.eq("status", filtro.getStatusPedido()));
		}

		return criteria;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pedido> filtrados(Filtro filtro){

		Criteria criteria =  criarCriteria(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		}else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}

		criteria.setResultTransformer(new AliasToBeanResultTransformer(Pedido.class));

		return criteria.list();
	}

	@Override
	public void deletaParcelasPedido(Pedido pedido){

		try{
			em.getTransaction().begin();

			String sql = "delete from parcela p where p.pedido_id = :id";
			Query query = em.createNativeQuery(sql);
			query.setParameter("id", pedido.getId().longValue());
			query.executeUpdate();

			em.getTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}

	}

	@Override
	public void salvarPedido(Pedido pedido){
		try{
			em.getTransaction().begin();

			if(pedido.getId() != null)
				em.merge(pedido);
			else
				em.persist(pedido);

			if (pedido.getProdutos() != null) {

				for (Produto produto : pedido.getProdutos()) {
					if (produto.getId() != null)
						em.merge(produto);
					else
						em.persist(produto);
				}
			}

			if (pedido.getParcelas() != null ) {

				for (Parcela parcela : pedido.getParcelas()) {
					if (parcela.getId() != null)
						em.merge(parcela);
					else
						em.persist(parcela);
				}
			}

			em.getTransaction().commit();
		}catch (Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}
	}

}
