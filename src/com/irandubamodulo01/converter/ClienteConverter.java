package com.irandubamodulo01.converter;

import com.irandubamodulo01.dao.*;
import com.irandubamodulo01.daoimpl.*;
import com.irandubamodulo01.model.Cliente;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.util.CDILocator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(forClass = Cliente.class)
public class ClienteConverter implements Converter {

	private ClienteDAO clienteDAO;

	public ClienteConverter(){
		this.clienteDAO = CDILocator.getBean(ClienteDAOImpl.class);
	}
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent uicomponent, String string) {
		 if (string == null || string.isEmpty() ) {
			return null;
		}
		 
		Cliente cliente = clienteDAO.getById(Cliente.class, Long.valueOf(string));
		 
		return cliente;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uicomponent, Object object) {
		Cliente cliente = (Cliente) object;
		 if (cliente == null || cliente.getId() == null) {
			return null;
		}
		 
	 return String.valueOf(cliente.getId());
	}

}
