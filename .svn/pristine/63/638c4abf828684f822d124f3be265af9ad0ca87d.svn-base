package com.irandubamodulo01.daoimpl;

import com.irandubamodulo01.dao.BarcoDAO;
import com.irandubamodulo01.dao.ProdutoDAO;
import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Pedido;
import com.irandubamodulo01.model.Produto;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ProdutoDaoImpl extends DAOimpl<Produto, Long> implements ProdutoDAO{


	private EntityManager em;

	@Inject
	public ProdutoDaoImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public List<Produto> getProdutosPorPedido(Pedido pedido){
		String jpql = "select p from Produto p where p.pedido.id = :id order by p.id asc";
		Query query = em.createQuery(jpql);  
		query.setParameter("id", pedido.getId().longValue());
		return query.getResultList();
	}

}
