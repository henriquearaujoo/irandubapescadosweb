package com.irandubamodulo01.daoimpl;

import com.irandubamodulo01.dao.BarcoDAO;
import com.irandubamodulo01.dao.UltimoLoginDAO;
import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.UltimoLogin;
import com.irandubamodulo01.model.Usuario;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class UltimoLoginDaoImpl extends DAOimpl<UltimoLogin, Long> implements UltimoLoginDAO{


	private EntityManager em;

	@Inject
	public UltimoLoginDaoImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public UltimoLogin getUltimoLoginUsuario(Usuario usuario){
		String jpql = "select ul from UltimoLogin ul where ul.usuario.id = :id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", usuario.getId().longValue());
		List<UltimoLogin> list = query.getResultList();
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

}
