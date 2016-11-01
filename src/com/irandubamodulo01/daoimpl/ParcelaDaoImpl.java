package com.irandubamodulo01.daoimpl;

import com.irandubamodulo01.dao.ParcelaDAO;
import com.irandubamodulo01.dao.ProdutoDAO;
import com.irandubamodulo01.model.Parcela;
import com.irandubamodulo01.model.Pedido;
import com.irandubamodulo01.model.Produto;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ParcelaDaoImpl extends DAOimpl<Parcela, Long> implements ParcelaDAO{


	private EntityManager em;

	@Inject
	public ParcelaDaoImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public List<Parcela> getParcelasPorPedido(Pedido pedido){
		String jpql = "from Parcela p where p.pedido.id = :id order by p.numeroParcela asc";
		Query query = em.createQuery(jpql);  
		query.setParameter("id", pedido.getId().longValue());
		return query.getResultList();
	}

}
