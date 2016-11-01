package com.irandubamodulo01.daoimpl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.irandubamodulo01.annotation.Transactional;
import com.irandubamodulo01.dao.LoteDAO;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Lote;


public class LoteDAOImpl extends DAOimpl<Lote, Long> implements LoteDAO{
	
	
	private EntityManager em;
	
	@Inject
	public LoteDAOImpl(EntityManager em){
		this.em = em;
	}
	
	@Transactional
	@Override
	public void salvarLotes(List<Lote> lotes){
		//em = getEntityManager();			
		for (Lote lote : lotes) {
			em.persist(lote);
		}

	}

	@Override
	public List<Lote> buscarLotePorCompra(Compra compra) {
		String jpql = "select l from Lote l where l.compra.id = :id";
		Query query =  em.createQuery(jpql);
		query.setParameter("id", compra.getId());
		return query.getResultList();
	}
	
	@Override
	public List<Lote> buscarLotePorCompraa(Compra compra) {
		String jpql = "select new Lote(l.id, l.valorUnitarioPeixe, l.peso, l.qtdCaixas, l.pesoCacapa, p.id, p.descricao, p.valor, l.valor, l.acrescimo, l.desconto, l.isPrecoDiferenciado) "
				+ "from Lote l left join l.peixe as p where l.compra.id = :id order by p.descricao";
		Query query =  em.createQuery(jpql);
		query.setParameter("id", compra.getId());
		return query.getResultList();
	}
	
	

}
