package com.irandubamodulo01.daoimpl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.irandubamodulo01.dao.CamaraDAO;
import com.irandubamodulo01.dao.PosicaoCamaraDAO;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.PosicaoCamara;

public class PosicaoCamaraDAOImpl extends DAOimpl<PosicaoCamara, Long> implements PosicaoCamaraDAO{

	private EntityManager em;
	
	@Inject
	public PosicaoCamaraDAOImpl(EntityManager em){
		this.em = em;
	}
	
	public PosicaoCamaraDAOImpl(){}
	 
	@Override
	public List<PosicaoCamara> getPosicoesByCamara(Camara camara){
		String jpql = "from PosicaoCamara pc where pc.camara.id =  :id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", camara.getId().longValue());
		return query.getResultList();
	}

	@Override
	public List<PosicaoCamara> getPosicoesComplete(){
		String jpql = "select new PosicaoCamara(p.id, p.descricao) from PosicaoCamara p order by p.descricao";
		Query query = em.createQuery(jpql);
		return query.getResultList();
	}
}
