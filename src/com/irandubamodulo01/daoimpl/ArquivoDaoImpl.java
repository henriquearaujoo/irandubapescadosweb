package com.irandubamodulo01.daoimpl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.irandubamodulo01.dao.ArquivoDAO;
import com.irandubamodulo01.dao.BarcoDAO;
import com.irandubamodulo01.model.*;

public class ArquivoDaoImpl extends DAOimpl<Arquivo, Long> implements ArquivoDAO{


	private EntityManager em;
	
	@Inject
	public ArquivoDaoImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public List<Arquivo> getArquivosPorPagamento(Pagamento pagamento){
		String jpql = "select new Arquivo(a.id, a.nome, a.tipo) from Arquivo a where a.pagamento.id = :id";
		Query query = em.createQuery(jpql);  
		query.setParameter("id", pagamento.getId().longValue());
		return query.getResultList();
	}

	@Override
	public List<Arquivo> getArquivosPorPeixe(Peixe peixe){
		String jpql = "select new Arquivo(a.id, a.nome, a.tipo) from Arquivo a where a.peixe.id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", peixe.getId().longValue());
		return query.getResultList();
	}

	@Override
	public List<Arquivo> getArquivosPorPedido(Pedido pedido){
		String jpql = "select new Arquivo(a.id, a.nome, a.tipo) from Arquivo a where a.pedido.id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", pedido.getId().longValue());
		return query.getResultList();
	}

	@Override
	public List<Arquivo> getArquivosPorAdiantamento(AdiantamentoFornecedor adiantamento){
		String jpql = "select new Arquivo(a.id, a.nome, a.tipo) from Arquivo a where a.adiantamento.id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", adiantamento.getId().longValue());
		return query.getResultList();
	}

}
