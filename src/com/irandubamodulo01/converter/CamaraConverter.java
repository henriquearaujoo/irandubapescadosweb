package com.irandubamodulo01.converter;

import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.model.DefaultTreeNode;

import com.irandubamodulo01.dao.CamaraDAO;
import com.irandubamodulo01.dao.CompraDAO;
import com.irandubamodulo01.dao.HistoricoAlteracaoValorDao;
import com.irandubamodulo01.dao.LoteDAO;
import com.irandubamodulo01.dao.ObservacaoDAO;
import com.irandubamodulo01.dao.PosicaoCamaraDAO;
import com.irandubamodulo01.daoimpl.CamaraDAOImpl;
import com.irandubamodulo01.daoimpl.CompraDAOImpl;
import com.irandubamodulo01.daoimpl.HistoricoAlteracaoValorDaoImpl;
import com.irandubamodulo01.daoimpl.LoteDAOImpl;
import com.irandubamodulo01.daoimpl.ObservacaoDaoImpl;
import com.irandubamodulo01.daoimpl.PosicaoCamaraDAOImpl;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.HistoricoAlteracaoValor;
import com.irandubamodulo01.model.Lote;
import com.irandubamodulo01.model.Observacao;
import com.irandubamodulo01.model.PosicaoCamara;
import com.irandubamodulo01.service.ServiceTreeTable;
import com.irandubamodulo01.util.CDILocator;

@FacesConverter(forClass = Camara.class)
public class CamaraConverter implements Converter{
	
	private CamaraDAO camaraDao;
	private PosicaoCamaraDAO posicaoDao;
	
	public CamaraConverter() {
	   camaraDao = CDILocator.getBean(CamaraDAOImpl.class);
	   posicaoDao =  CDILocator.getBean(PosicaoCamaraDAOImpl.class);
	  
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String string) {
		
		 if (string == null || string.isEmpty() ) {
				return null;
			}
		   
		Camara camara = camaraDao.getById(Camara.class, Long.valueOf(string));
	 	camara.setPosicoes(new ArrayList<>());
	 	camara.setPosicoes(posicaoDao.getPosicoesByCamara(camara));
		return camara; 
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		Camara camara = (Camara) object;
		if (camara == null || camara.getId() == null) {
			 return null;
		}
		
		return String.valueOf(camara.getId());
	}

	
	
}
