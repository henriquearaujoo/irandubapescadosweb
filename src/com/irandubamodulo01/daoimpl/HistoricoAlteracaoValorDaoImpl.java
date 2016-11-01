package com.irandubamodulo01.daoimpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.irandubamodulo01.annotation.Transactional;
import com.irandubamodulo01.dao.HistoricoAlteracaoValorDao;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.HistoricoAlteracaoValor;
import com.irandubamodulo01.model.Lote;

public class HistoricoAlteracaoValorDaoImpl extends DAOimpl<HistoricoAlteracaoValor, Long> implements HistoricoAlteracaoValorDao{

	private EntityManager em;
	
	@Inject
	public HistoricoAlteracaoValorDaoImpl(EntityManager em){
		this.em = em;
	}
	
	public HistoricoAlteracaoValorDaoImpl(){}
	
	@Transactional
	@Override
	public void salvarHistoricoEmLotes(List<HistoricoAlteracaoValor> historicos){
		
		try {
			for (HistoricoAlteracaoValor historicoAlteracaoValor : historicos) {
				em.merge(historicoAlteracaoValor);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<HistoricoAlteracaoValor> getHistoricoPorCompra(Long id) {
		try {
			String jpql = "select his from HistoricoAlteracaoValor his where his.compra.id = :id";
			Query query = em.createQuery(jpql);
			query.setParameter("id",id);
			return query.getResultList();	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<HistoricoAlteracaoValor> getHistorico(Compra compra){
		
		List<HistoricoAlteracaoValor> list = new ArrayList<HistoricoAlteracaoValor>();
		
		String jpql = "from HistoricoAlteracaoValor where compra.id = :idCompra";
		Query query = em.createQuery(jpql);
		query.setParameter("idCompra", compra.getId());
		list = query.getResultList();
		
		return list;
		
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public void removeAlteracaoLote(Lote lote){
		String jpql =  "delete from HistoricoAlteracaoValor where lote.id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", lote.getId().longValue());
		query.executeUpdate();
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<HistoricoAlteracaoValor> getHistoricoPorTipoEcompra(Compra compra){
		
		List<HistoricoAlteracaoValor> list = new ArrayList<HistoricoAlteracaoValor>();
		
		String jpql = "from HistoricoAlteracaoValor where compra.id = :idCompra and tipo = :acrescimo";
		Query query = em.createQuery(jpql);
		query.setParameter("idCompra", compra.getId());
		query.setParameter("acrescimo", "ACRESCIMO");
		list = query.getResultList();
		
		return list;
		
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public void removeAlteracaoTotal(Compra compra) {
		String jpql =  "delete from HistoricoAlteracaoValor where compra.id = :id and lote is  null";
		Query query = em.createQuery(jpql);
		query.setParameter("id", compra.getId().longValue());
		query.executeUpdate();
	}
	
	
}
