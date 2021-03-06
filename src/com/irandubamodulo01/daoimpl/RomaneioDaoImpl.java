package com.irandubamodulo01.daoimpl;

import com.irandubamodulo01.dao.BarcoDAO;
import com.irandubamodulo01.dao.RomaneioDAO;
import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Produto;
import com.irandubamodulo01.model.Romaneio;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class RomaneioDaoImpl extends DAOimpl<Romaneio, Long> implements RomaneioDAO{


	private EntityManager em;

	@Inject
	public RomaneioDaoImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public List<Romaneio> getRomaneiosPorProduto(Produto produto){
		String jpql = "select new Romaneio(r.id, r.lote, r.peso, r.qtdeEmbalagens, r.tamanhoPeixe.id, r.tamanhoPeixe.descricao, r.camara.id, r.camara.descricao, r.posicaoCamara.id, r.posicaoCamara.descricao) from Romaneio r where r.produto.id = :id";
		Query query = em.createQuery(jpql);  
		query.setParameter("id", produto.getId().longValue());
		return query.getResultList();
	}

}
