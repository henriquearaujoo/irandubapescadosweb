package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.irandubamodulo01.dao.CamaraDAO;
import com.irandubamodulo01.dao.TipoPeixeDAO;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.TipoPeixe;
import com.irandubamodulo01.util.Filtro;

@Named(value =  "bean")
@ViewScoped
public class BeanTest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject CamaraDAO tipoPeixeDao;
	private @Inject TipoPeixe tipoPeixe;
	private Filtro filtro =  new Filtro();
	private LazyDataModel<Camara> model;
	
	public BeanTest(){
		
		model = new LazyDataModel<Camara>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<Camara> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				setRowCount(tipoPeixeDao.quantidadeFiltrados(filtro));
				return tipoPeixeDao.filtrados(filtro);
			}
		};
		
	}

	public LazyDataModel<Camara> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Camara> model) {
		this.model = model;
	}



}
