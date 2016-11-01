package com.irandubamodulo01.converter;

import com.irandubamodulo01.dao.*;
import com.irandubamodulo01.daoimpl.*;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Transportadora;
import com.irandubamodulo01.util.CDILocator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(forClass = Transportadora.class)
public class TransportadoraConverter implements Converter {

	private TransportadoraDAO transportadoraDAO;

	public TransportadoraConverter(){
		this.transportadoraDAO = CDILocator.getBean(TransportadoraDAOImpl.class);
	}
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent uicomponent, String string) {
		 if (string == null || string.isEmpty() ) {
			return null;
		}
		 
		Transportadora transportadora = transportadoraDAO.getById(Transportadora.class, Long.valueOf(string));
		 
		return transportadora;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uicomponent, Object object) {
		Transportadora transportadora = (Transportadora) object;
		 if (transportadora == null || transportadora.getId() == null) {
			return null;
		}
		 
	 return String.valueOf(transportadora.getId());
	}

	
	
	
}
