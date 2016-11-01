package com.irandubamodulo01.daoimpl;

import com.irandubamodulo01.dao.HistoricoPedidoDAO;
import com.irandubamodulo01.dao.ObservacaoDAO;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.HistoricoPedido;
import com.irandubamodulo01.model.Observacao;
import com.irandubamodulo01.model.Pedido;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class HistoricoPedidoDaoImpl extends DAOimpl<HistoricoPedido, Long> implements HistoricoPedidoDAO{

	private EntityManager em;

	public HistoricoPedidoDaoImpl() {

	}

	@Inject
	public HistoricoPedidoDaoImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public List<HistoricoPedido> getHistoricoPorPedido(Pedido pedido){
		String jpql = "from HistoricoPedido where pedido.id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", pedido.getId().longValue());
		return query.getResultList();
		
	}
}
