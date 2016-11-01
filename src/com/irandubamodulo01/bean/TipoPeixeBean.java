package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.irandubamodulo01.dao.CamaraDAO;
import com.irandubamodulo01.dao.PosicaoCamaraDAO;
import com.irandubamodulo01.dao.TipoPeixeDAO;
import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.PosicaoCamara;
import com.irandubamodulo01.model.TipoPeixe;
import com.irandubamodulo01.util.Filtro;

@Named(value = "tipoPeixeBean")
@ViewScoped
public class TipoPeixeBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject TipoPeixeDAO tipoPeixeDao;
	private @Inject TipoPeixe tipoPeixe;
	private Filtro filtro =  new Filtro();
	private LazyDataModel<TipoPeixe> model;
	

	
	public TipoPeixeBean(){
		
		model = new LazyDataModel<TipoPeixe>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<TipoPeixe> load(int first, int pageSize, String sortField,
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
	
	public void prepararCad(){
		tipoPeixe = new TipoPeixe();
	}

	public void salvar(){
	    try{
			tipoPeixeDao.save(tipoPeixe);
			tipoPeixe = new TipoPeixe();
			addMessage("Informação", "Tipo salvo com sucesso.", FacesMessage.SEVERITY_INFO);
	    }catch(Exception e){
			e.printStackTrace();
	    	addMessage("Erro", "Não foi possível salvar o tipo.", FacesMessage.SEVERITY_ERROR);
	    }
	} 
	
	public void delete(){
		try{
			tipoPeixeDao.remove(tipoPeixe);
			addMessage("Informação", "Tipo excluído com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível excluir o tipo.", FacesMessage.SEVERITY_ERROR);
		}

	}
	
	
	public void addMessage(String summary, String detail, Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	
	//Getters and Setters
	public TipoPeixeDAO getTipoPeixeDao() {
		return tipoPeixeDao;
	}

	public void setTipoPeixeDao(TipoPeixeDAO tipoPeixeDao) {
		this.tipoPeixeDao = tipoPeixeDao;
	}

	public TipoPeixe getTipoPeixe() {
		return tipoPeixe;
	}

	public void setTipoPeixe(TipoPeixe tipoPeixe) {
		this.tipoPeixe = tipoPeixe;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public LazyDataModel<TipoPeixe> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<TipoPeixe> model) {
		this.model = model;
	}
	
	

	
}
