package com.irandubamodulo01.daoimpl;

import com.irandubamodulo01.dao.BarcoDAO;
import com.irandubamodulo01.dao.PrecoDiferenciadoDAO;
import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Peixe;
import com.irandubamodulo01.model.PrecoDiferenciado;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class PrecoDiferenciadoDAOImpl extends DAOimpl<PrecoDiferenciado, Long> implements PrecoDiferenciadoDAO{


	private EntityManager em;

	@Inject
	public PrecoDiferenciadoDAOImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public List<PrecoDiferenciado> getPrecoDiferenciadoPorFornecedor(Fornecedor fornecedor){
		String jpql = "select p from PrecoDiferenciado p where p.fornecedor.id = :id order by p.peixe.descricao";
		Query query = em.createQuery(jpql);  
		query.setParameter("id", fornecedor.getId().longValue());
		return query.getResultList();
	}

	@Override
	public List<PrecoDiferenciado> getPrecoDiferenciadoPorFornecedorPeixeDif(Fornecedor fornecedor, List<Long> idsPeixes){
		String jpql = "";

		if (idsPeixes.size() > 0)
			jpql = "select p from PrecoDiferenciado p where p.fornecedor.id = :id and p.peixe.id not in (:ids) order by p.peixe.descricao";
		else
			jpql = "select p from PrecoDiferenciado p where p.fornecedor.id = :id order by p.peixe.descricao";

		Query query = em.createQuery(jpql);
		query.setParameter("id", fornecedor.getId().longValue());

		if (idsPeixes.size() > 0)
			query.setParameter("ids", idsPeixes);

		return query.getResultList();
	}

	@Override
	public PrecoDiferenciado getPrecoDiferenciadoPorFornecedorEPeixe(Fornecedor fornecedor, Peixe peixe){
		String jpql = "select p from PrecoDiferenciado p where p.fornecedor.id = :id and p.peixe.id = :idPeixe order by p.peixe.descricao";
		Query query = em.createQuery(jpql);
		query.setParameter("id", fornecedor.getId().longValue());
		query.setParameter("idPeixe", peixe.getId().longValue());
		return query.getResultList() != null && query.getResultList().size() > 0 ? (PrecoDiferenciado) query.getResultList().get(0) : null;
	}
}
