package com.irandubamodulo01.daoimpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.irandubamodulo01.model.*;
import com.irandubamodulo01.util.EstoqueUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.irandubamodulo01.dao.ArmazenamentoDAO;
import com.irandubamodulo01.enumerated.StatusArmazenamento;
import com.irandubamodulo01.util.Filtro;

public class ArmazenamentoDAOImpl extends DAOimpl<Armazenamento, Long> implements ArmazenamentoDAO{

	private EntityManager em;
	
	@Inject
	public ArmazenamentoDAOImpl(EntityManager em){
		this.em = em;
	}
	
	public ArmazenamentoDAOImpl(){}
	
	
	@Override
	public List<Armazenamento> filtrados(Filtro filtro) {
		Criteria criteria = criarCriteria(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		} else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}

		return criteria.list();
	}

	@Override
	public int quantidadeFiltrados(Filtro filtro) {
		 Criteria criteria = criarCriteria(filtro);
		 criteria.setProjection(Projections.rowCount());
		 return ((Number)criteria.uniqueResult()).intValue();
	}

	private Criteria criarCriteria(Filtro filtro) {
		//	em = JPAUtil.getEntityManager();
		Session session = em.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Armazenamento.class);

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null){
			criteria.add(Restrictions.between("data", filtro.getDataInicio(), filtro.getDataFinal()));
		}else if (filtro.getDataInicio() != null && filtro.getDataFinal() == null){
			criteria.add(Restrictions.sqlRestriction("to_char(data, 'dd/MM/yyyy') = '" + new SimpleDateFormat("dd/MM/yyyy").format(filtro.getDataInicio()) + "'"));
		}else if (filtro.getDataInicio() == null && filtro.getDataFinal() != null){
			criteria.add(Restrictions.sqlRestriction("to_char(data, 'dd/MM/yyyy') = '" + new SimpleDateFormat("dd/MM/yyyy").format(filtro.getDataFinal()) + "'"));
		}
		
		if (filtro.getFiltroStatus() != null) {
			criteria.add(Restrictions.eq("status", filtro.getFiltroStatus()));
		}
		if (filtro.getUsuario() != null)
		if (!filtro.getUsuario().getPerfil().getDescricao().equals("Admin")) {
			String sql = "status != '"+StatusArmazenamento.RETORNADO+"'";
			criteria.add(Restrictions.sqlRestriction(sql));
		}

		return criteria;
	}

	@Override
	public BigDecimal getPesoDisponivelPorPeixe(Peixe peixe){
		BigDecimal peso = BigDecimal.ZERO;
		String sql = "select coalesce( " +
				"(select coalesce(sum(arm.peso),0) from armazenamento arm where arm.peixe_id = p.id and arm.status = 'AUTORIZADO') " +
				"- " +
				"(select coalesce(sum(ret.peso),0) from retirada ret where ret.peixe_id = p.id and ret.status = 'AUTORIZADO' ), 0) as peso " +
				"from  peixe p " +
				"join armazenamento arm1 on p.id = arm1.peixe_id " +
				"where arm1.status = 'AUTORIZADO' and p.id = :id " +
				"group by p.id, p.descricao";
		Query query = em.createNativeQuery(sql);
		query.setParameter("id", peixe.getId());
		List<Object> list = query.getResultList();
		if (list != null && list.size() > 0)
			peso = (BigDecimal) list.get(0);
		return peso;
	}

	public BigDecimal getPesoDisponivelPorPeixeTipo(Peixe peixe, TipoPeixe tipoPeixe){
		BigDecimal peso = BigDecimal.ZERO;
		String sql = "select coalesce( " +
				"(select coalesce(sum(arm.peso),0) from armazenamento arm where arm.peixe_id = p.id and arm.tipopeixe_id = tp.id and arm.status = 'AUTORIZADO') " +
				"- " +
				"(select coalesce(sum(ret.peso),0) from retirada ret where ret.peixe_id = p.id and ret.tipopeixe_id = tp.id and ret.status = 'AUTORIZADO' ), 0) as peso  " +
				"from  peixe p " +
				"join armazenamento arm1 on p.id = arm1.peixe_id " +
				"join tipopeixe tp on tp.id = arm1.tipopeixe_id " +
				"where arm1.status = 'AUTORIZADO' and p.id = :id and tp.id = :idtipo " +
				"group by p.id, p.descricao, tp.id";
		Query query = em.createNativeQuery(sql);
		query.setParameter("id", peixe.getId());
		query.setParameter("idtipo", tipoPeixe.getId());
		List<Object> list = query.getResultList();
		if (list != null && list.size() > 0)
			peso = (BigDecimal) list.get(0);
		return peso;
	}

	@Override
	public List<EstoqueUtil> getEstoquePorPeixeCamaraTipo(Peixe peixe, Camara camara, TipoPeixe tipoPeixe){
		String sql = "select p.id as idPeixe, p.descricao as peixe, c.id as idCamara, c.descricao as camara, pc.id as idPosicao, pc.descricao as posicao, " +
				" tp.id as idTipo, tp.descricao as tipo, tmp.id as idTamanho, tmp.descricao as tamanho, coalesce(sum(a.peso), 0) as peso, " +
				" (select coalesce(sum(r.peso),0) from retirada r " +
				" where r.status = 'AUTORIZADO' and r.peixe_id = p.id and r.tipopeixe_id = tp.id and r.tamanhopeixe_id = tmp.id and r.camara_id = c.id and r.posicaocamara_id = pc.id) as peso_retirada " +
				" from armazenamento a " +
				" join peixe p on p.id = a.peixe_id " +
				" join tipopeixe tp on tp.id = a.tipopeixe_id " +
				" left join tamanhopeixe tmp on tmp.id = a.tamanhopeixe_id " +
				" join camara c on c.id = a.camara_id " +
				" join posicaocamara pc on pc.id = a.posicaocamara_id " +
				" where a.status = 'AUTORIZADO' ";

		if (tipoPeixe != null && tipoPeixe.getId() != null)
			sql += " and tp.id = :idTipo ";

		if (peixe != null && peixe.getId() != null)
			sql += " and p.id = :idPeixe";

		if (camara != null && camara.getId() != null)
			sql += " and c.id = :idCamara";

		/*if (filtro.getDataInicial() != null && !filtro.getDataInicial().isEmpty()
				&& filtro.getDataFinal() != null && !filtro.getDataFinal().isEmpty()){
			if (filtro.getDataInicial().equals(filtro.getDataFinal())){
				sql += "and to_char(a.data,'dd/MM/yyyy') = '" + filtro.getDataInicial() + "'";
			}else{
				sql += "and to_char(a.data,'dd/MM/yyyy') between '" + filtro.getDataInicial() + "' and '" + filtro.getDataFinal()+ "'";
			}
		}else if (filtro.getDataInicial() != null && !filtro.getDataInicial().isEmpty()
				&& filtro.getDataFinal() == null ){

			sql += "and to_char(a.data,'dd/MM/yyyy') = '" + filtro.getDataInicial() + "'";

		}else if (filtro.getDataInicial() == null
				&& filtro.getDataFinal() != null && !filtro.getDataFinal().isEmpty()){

			sql += "and to_char(a.data,'dd/MM/yyyy') between to_char(current_date, 'dd/MM/yyyy') and '" + filtro.getDataFinal()+ "'";
		}*/

		sql += "  group by p.id, p.descricao, c.id, c.descricao, pc.id, pc.descricao, tp.id, tp.descricao, tmp.id, tmp.descricao order by tp.descricao asc";

		Query query = em.createNativeQuery(sql);

		if (tipoPeixe != null && tipoPeixe.getId() != null)
			query.setParameter("idTipo", tipoPeixe.getId());

		if (peixe != null && peixe.getId() != null)
			query.setParameter("idPeixe", peixe.getId());

		if (camara != null && camara.getId() != null)
			query.setParameter("idCamara", camara.getId());

		List<EstoqueUtil> itens = new ArrayList<EstoqueUtil>();

		List<Object[]> list = query.getResultList();

		for(Object[] obj : list){
			try{
				EstoqueUtil itemEstoque = new EstoqueUtil();

				Peixe peixe1 = new Peixe();
				peixe1.setId(Long.parseLong(obj[0].toString()));
				peixe1.setDescricao(obj[1].toString());

				Camara camara1 = new Camara();
				camara1.setId(Long.parseLong(obj[2].toString()));
				camara1.setDescricao(obj[3].toString());

				PosicaoCamara posicaoCamara = new PosicaoCamara();
				posicaoCamara.setId(Long.parseLong(obj[4].toString()));
				posicaoCamara.setDescricao(obj[5].toString());

				TipoPeixe tipoPeixe1 = new TipoPeixe();
				tipoPeixe1.setId(Long.parseLong(obj[6].toString()));
				tipoPeixe1.setDescricao(obj[7].toString());

				TamanhoPeixe tamanhoPeixe = new TamanhoPeixe();
				tamanhoPeixe.setId(Long.parseLong(obj[8].toString()));
				tamanhoPeixe.setDescricao(obj[9].toString());

				itemEstoque.setPeixe(peixe1);
				itemEstoque.setCamara(camara1);
				itemEstoque.setPosicaoCamara(posicaoCamara);
				//itemEstoque.setDataArmazenamento(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(obj[6].toString()));
				itemEstoque.setTipo(tipoPeixe1);
				itemEstoque.setTamanho(tamanhoPeixe);
				itemEstoque.setPeso(new BigDecimal(obj[10].toString()));
				itemEstoque.setPesoRetirada(new BigDecimal(obj[11].toString()));

				itens.add(itemEstoque);
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		return itens;
	}
	
	@Override
	public Armazenamento obterArmazenamentoPorId(Long id){
		String hql = "from Armazenamento where id = " + id;
		Query query = em.createQuery(hql);
		return (Armazenamento) query.getSingleResult();
	}

	
}
