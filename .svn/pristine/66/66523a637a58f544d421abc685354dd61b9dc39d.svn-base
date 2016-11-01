package com.irandubamodulo01.converter;


import com.irandubamodulo01.dao.RetiradaDAO;
import com.irandubamodulo01.daoimpl.RetiradaDAOImpl;
import com.irandubamodulo01.model.Armazenamento;
import com.irandubamodulo01.model.Retirada;
import com.irandubamodulo01.util.CDILocator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Retirada.class)
public class RetiradaConverter implements Converter{

	private RetiradaDAO retiradaDAO;


	public RetiradaConverter() {
	   retiradaDAO = CDILocator.getBean(RetiradaDAOImpl.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String string) {
		
		 if (string == null || string.isEmpty() ) {
				return null;
			}
		   
		Retirada retirada = retiradaDAO.getById(Retirada.class, Long.valueOf(string));

		return retirada;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		Retirada retirada = (Retirada) object;
		if (retirada == null || retirada.getId() == null) {
			 return null;
		}
		
		return String.valueOf(retirada.getId());
	}

	
	
}
