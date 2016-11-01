package com.irandubamodulo01.daoimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.irandubamodulo01.dao.AdiantamentoDAO;
import com.irandubamodulo01.model.AdiantamentoFornecedor;
import com.irandubamodulo01.model.Conta;
import com.irandubamodulo01.model.Fornecedor;

public class AdiantamentoDAOImpl extends DAOimpl<AdiantamentoFornecedor, Long> implements AdiantamentoDAO{

	private EntityManager em;
	
	@Inject
	public  AdiantamentoDAOImpl(EntityManager em){
		this.em = em;
	}
	
	@Override
	public List<AdiantamentoFornecedor> getAdiantamentoPorFornecedor(
			Fornecedor fornecedor) {
		String jpql = "select ad from AdiantamentoFornecedor ad where ad.fornecedor.id = :id order by ad.data desc";
		Query query = em.createQuery(jpql);  
		query.setParameter("id", fornecedor.getId().longValue());
		
		return query.getResultList();
	}

	@Override
	public List<AdiantamentoFornecedor> getChequesDisponiveisAdiantamento(Fornecedor fornecedor){
		String sql = "select id, nomecheque, numerocheque, valor from adiantamento_fornecedor where nomecheque is not null and numerocheque is not null and tipo = 'CHEQUE' and fornecedor_id = :idFornecedor " +
						"order by data desc";
					 //" and id not in (select adiantamento_id from pagamento where tipopagamento = 'ADIANTAMENTO' and adiantamento_id is not null and pendente = false) ";

		Query query = em.createNativeQuery(sql);
		query.setParameter("idFornecedor", fornecedor.getId());

		List<AdiantamentoFornecedor> cheques = new ArrayList<AdiantamentoFornecedor>();

		List<Object[]> list = query.getResultList();

		for(Object[] obj : list){
			AdiantamentoFornecedor a = new AdiantamentoFornecedor();
			a.setId(Long.parseLong(obj[0].toString()));
			a.setNomeCheque(obj[1].toString());
			a.setNumeroCheque(obj[2].toString());
			a.setValor(new BigDecimal(obj[3].toString()));

			cheques.add(a);
		}

		return cheques;
	}

	@Override
	public List<AdiantamentoFornecedor> getContasDisponiveisAdiantamento(Fornecedor fornecedor){
		String sql = "select ad.id as idAdiantamento, c.id as idConta, c.agencia, c.conta, c.beneficiado, ad.valor " +
						"from adiantamento_fornecedor ad " +
						"join conta c on c.id = ad.conta_id " +
						"where ad.conta_id is not null  " +
						"and ad.fornecedor_id = :idFornecedor " +
						"order by ad.data desc";
						//"and ad.id not in (select adiantamento_id from pagamento where tipopagamento = 'ADIANTAMENTO' and adiantamento_id is not null and pendente = false) ";

		Query query = em.createNativeQuery(sql);
		query.setParameter("idFornecedor", fornecedor.getId());

		List<AdiantamentoFornecedor> contas = new ArrayList<AdiantamentoFornecedor>();

		List<Object[]> list = query.getResultList();

		for(Object[] obj : list){
			AdiantamentoFornecedor a = new AdiantamentoFornecedor();
			a.setId(Long.parseLong(obj[0].toString()));
			a.setConta(new Conta());
			a.getConta().setId(Long.parseLong(obj[1].toString()));
			a.getConta().setAgencia(obj[2].toString());
			a.getConta().setConta(obj[3].toString());
			a.getConta().setBeneficiado(obj[4].toString());
			a.setValor(new BigDecimal(obj[5].toString()));

			contas.add(a);
		}

		return contas;
	}
}
