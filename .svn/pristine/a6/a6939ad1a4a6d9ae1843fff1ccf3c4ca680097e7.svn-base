package com.irandubamodulo01.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.irandubamodulo01.dao.*;
import com.irandubamodulo01.daoimpl.*;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.util.CDILocator;

import java.util.function.Predicate;


@FacesConverter(forClass=Fornecedor.class)
public class FornecedorConverter implements Converter {

	private FornecedorDAO fornecDAO;
	private BarcoDAO barcoDAO;
	private AdiantamentoDAO adiantamentoDAO;
	private ContaDAO contaDAO;
	private PrecoDiferenciadoDAO precoDiferenciadoDAO;
	
	public FornecedorConverter(){
		this.fornecDAO = CDILocator.getBean(FornecedorDAOImpl.class);
		this.barcoDAO = CDILocator.getBean(BarcoDaoImpl.class);
		this.adiantamentoDAO = CDILocator.getBean(AdiantamentoDAOImpl.class);
		this.contaDAO = CDILocator.getBean(ContaDaoImpl.class);
		this.precoDiferenciadoDAO = CDILocator.getBean(PrecoDiferenciadoDAOImpl.class);
	}
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent uicomponent, String string) {
		 if (string == null || string.isEmpty() ) {
			return null;
		}
		 
		Fornecedor fornecedor = fornecDAO.getById(Fornecedor.class, Long.valueOf(string));
		fornecedor.setBarcos(barcoDAO.getBarcosPorFornecedor(fornecedor));
		fornecedor.setAdiantamentos(adiantamentoDAO.getAdiantamentoPorFornecedor(fornecedor));
		fornecedor.setContas(contaDAO.getContasPorFornecedor(fornecedor));
		fornecedor.setPrecosDiferenciados(precoDiferenciadoDAO.getPrecoDiferenciadoPorFornecedor(fornecedor));
		 
		return fornecedor;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uicomponent, Object object) {
		 Fornecedor fornec = (Fornecedor) object;
		 if (fornec == null || fornec.getId() == null) {
			return null;
		}
		 
	 return String.valueOf(fornec.getId());
	}

	
	
	
}
