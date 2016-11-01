package com.irandubamodulo01.converter;

import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.model.DefaultTreeNode;

import com.irandubamodulo01.dao.CompraDAO;
import com.irandubamodulo01.dao.HistoricoAlteracaoValorDao;
import com.irandubamodulo01.dao.LoteDAO;
import com.irandubamodulo01.dao.ObservacaoDAO;
import com.irandubamodulo01.daoimpl.CompraDAOImpl;
import com.irandubamodulo01.daoimpl.HistoricoAlteracaoValorDaoImpl;
import com.irandubamodulo01.daoimpl.LoteDAOImpl;
import com.irandubamodulo01.daoimpl.ObservacaoDaoImpl;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.HistoricoAlteracaoValor;
import com.irandubamodulo01.model.Lote;
import com.irandubamodulo01.model.Observacao;
import com.irandubamodulo01.service.ServiceTreeTable;
import com.irandubamodulo01.util.CDILocator;

@FacesConverter(forClass = Compra.class)
public class CompraConverter implements Converter{
	
	private CompraDAO dao;
	private LoteDAO daoLote;
	private HistoricoAlteracaoValorDao daoHistorico;
	private ObservacaoDAO daoObs;
	
	public CompraConverter() {
	   dao = CDILocator.getBean(CompraDAOImpl.class);
	   daoLote = CDILocator.getBean(LoteDAOImpl.class);
	   daoHistorico = CDILocator.getBean(HistoricoAlteracaoValorDaoImpl.class);
	   daoObs = CDILocator.getBean(ObservacaoDaoImpl.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String string) {
		
		 if (string == null || string.isEmpty() ) {
				return null;
			}
		   
		Compra compra = dao.getById(Compra.class, Long.valueOf(string));
	  if (compra.getObservacaoVerificacao() != null)
		if (!compra.getObservacaoVerificacao().equalsIgnoreCase("")) {
			compra.setObservacoesSistema(new ArrayList<String>());
			String[]  vet =  compra.getObservacaoVerificacao().split(";");
			for (int i = 0; i < vet.length; i++) {
				compra.getObservacoesSistema().add(vet[i]);
			}
		}
		
		
		compra.setLotes(new ArrayList<Lote>());
		compra.setLotes(daoLote.buscarLotePorCompraa(compra));
		compra.setHistoricos(new ArrayList<HistoricoAlteracaoValor>());
		compra.setHistoricos(daoHistorico.getHistoricoPorCompra(compra.getId()));
		compra.setObservacoes(new ArrayList<Observacao>());
		compra.setObservacoes(daoObs.getObservacaoPorCompra(compra));
		compra.setTreeNode(new ServiceTreeTable().getRoot(compra.getLotes()));
		return compra; 
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		Compra compra = (Compra) object;
		if (compra == null || compra.getId() == null) {
			 return null;
		}
		
		return String.valueOf(compra.getId());
	}

	
	
}
