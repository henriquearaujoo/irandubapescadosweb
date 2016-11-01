package com.irandubamodulo01.converter;

import com.irandubamodulo01.dao.*;
import com.irandubamodulo01.daoimpl.*;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Vendedor;
import com.irandubamodulo01.util.CDILocator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(forClass = Vendedor.class)
public class VendedorConverter implements Converter {

	private VendedorDAO vendedorDAO;

	public VendedorConverter(){
		this.vendedorDAO = CDILocator.getBean(VendedorDAOImpl.class);
	}
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent uicomponent, String string) {
		 if (string == null || string.isEmpty() ) {
			return null;
		}
		 
		Vendedor vendedor = vendedorDAO.getById(Vendedor.class, Long.valueOf(string));
		 
		return vendedor;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uicomponent, Object object) {
		Vendedor vendedor = (Vendedor) object;
		 if (vendedor == null || vendedor.getId() == null) {
			return null;
		}
		 
	 return String.valueOf(vendedor.getId());
	}

	
	
	
}
