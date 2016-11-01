package com.irandubamodulo01.converter;

import com.irandubamodulo01.dao.PagamentoDAO;
import com.irandubamodulo01.dao.PeixeDAO;
import com.irandubamodulo01.daoimpl.PagamentoDAOImpl;
import com.irandubamodulo01.daoimpl.PeixeDAOImpl;
import com.irandubamodulo01.model.Pagamento;
import com.irandubamodulo01.model.Peixe;
import com.irandubamodulo01.util.CDILocator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(forClass=Pagamento.class)
public class PagamentoConverter implements Converter {

	private PagamentoDAO pDAO;

	public PagamentoConverter(){
		pDAO = CDILocator.getBean(PagamentoDAOImpl.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent uicomponent, String string) {
		 if (string == null || string.isEmpty() ) {
			return null;
		}
		return pDAO.getById(Pagamento.class, Long.valueOf(string));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uicomponent, Object object) {
		Pagamento pagamento = (Pagamento) object;
		 if (pagamento == null || pagamento.getId() == null) {
			return null;
		}
		 
		 return String.valueOf(pagamento.getId());
	}

}
