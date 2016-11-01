package com.irandubamodulo01.converter;

import com.irandubamodulo01.dao.AdiantamentoDAO;
import com.irandubamodulo01.dao.PagamentoDAO;
import com.irandubamodulo01.daoimpl.AdiantamentoDAOImpl;
import com.irandubamodulo01.daoimpl.PagamentoDAOImpl;
import com.irandubamodulo01.model.AdiantamentoFornecedor;
import com.irandubamodulo01.model.Pagamento;
import com.irandubamodulo01.util.CDILocator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(forClass=AdiantamentoFornecedor.class)
public class AdiantamentoConverter implements Converter {

	private AdiantamentoDAO adDAO;

	public AdiantamentoConverter(){
		adDAO = CDILocator.getBean(AdiantamentoDAOImpl.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent uicomponent, String string) {
		 if (string == null || string.isEmpty() ) {
			return null;
		}
		return adDAO.getById(AdiantamentoFornecedor.class, Long.valueOf(string));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uicomponent, Object object) {
		AdiantamentoFornecedor adiantamento = (AdiantamentoFornecedor) object;
		 if (adiantamento == null || adiantamento.getId() == null) {
			return null;
		}
		 
		 return String.valueOf(adiantamento.getId());
	}

}
