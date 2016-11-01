package com.irandubamodulo01.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.irandubamodulo01.dao.UsuarioDAO;
import com.irandubamodulo01.daoimpl.UsuarioDAOImpl;
import com.irandubamodulo01.model.Usuario;
import com.irandubamodulo01.util.CDILocator;
import com.irandubamodulo01.util.JPAUtil;

@FacesConverter(forClass=Usuario.class)
public class UsuarioConverter implements Converter{
	
	private UsuarioDAO usuDAO;
	
	public UsuarioConverter(){
		this.usuDAO = CDILocator.getBean(UsuarioDAOImpl.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent uicomponent, String string) {
		 if (string == null || string.isEmpty() ) {
			return null;
		}
		return usuDAO.getById(Usuario.class, Long.valueOf(string));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uicomponent, Object object) {
		 Usuario usuario = (Usuario) object;
		 if (usuario == null || usuario.getId() == null) {
			return null;
		}
	 return String.valueOf(usuario.getId());
	}

}
