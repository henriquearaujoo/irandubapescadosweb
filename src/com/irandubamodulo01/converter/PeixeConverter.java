package com.irandubamodulo01.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.irandubamodulo01.dao.PeixeDAO;
import com.irandubamodulo01.daoimpl.PeixeDAOImpl;
import com.irandubamodulo01.model.Peixe;
import com.irandubamodulo01.util.CDILocator;
import com.irandubamodulo01.util.JPAUtil;


@FacesConverter(forClass=Peixe.class)
public class PeixeConverter implements Converter {

	private PeixeDAO pDAO;
	
	public PeixeConverter(){
		pDAO = CDILocator.getBean(PeixeDAOImpl.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent uicomponent, String string) {
		 if (string == null || string.isEmpty() ) {
			return null;
		}
		return pDAO.getById(Peixe.class, Long.valueOf(string));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uicomponent, Object object) {
		 Peixe peixe = (Peixe) object;
		 if (peixe == null || peixe.getId() == null) {
			return null;
		}
		 
		 return String.valueOf(peixe.getId());
	}

}
