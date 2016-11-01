package com.irandubamodulo01.converter;

import com.irandubamodulo01.dao.ArmazenamentoDAO;
import com.irandubamodulo01.dao.PosicaoCamaraDAO;
import com.irandubamodulo01.daoimpl.ArmazenamentoDAOImpl;
import com.irandubamodulo01.daoimpl.CamaraDAOImpl;
import com.irandubamodulo01.daoimpl.PosicaoCamaraDAOImpl;
import com.irandubamodulo01.model.Armazenamento;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.util.CDILocator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.ArrayList;

@FacesConverter(forClass = Armazenamento.class)
public class ArmazenamentoConverter implements Converter{

	private ArmazenamentoDAO armazenamentoDAO;


	public ArmazenamentoConverter() {
	   armazenamentoDAO = CDILocator.getBean(ArmazenamentoDAOImpl.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String string) {
		
		 if (string == null || string.isEmpty() ) {
				return null;
			}
		   
		Armazenamento armazenamento = armazenamentoDAO.getById(Armazenamento.class, Long.valueOf(string));

		return armazenamento;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		Armazenamento armazenamento = (Armazenamento) object;
		if (armazenamento == null || armazenamento.getId() == null) {
			 return null;
		}
		
		return String.valueOf(armazenamento.getId());
	}

	
	
}
