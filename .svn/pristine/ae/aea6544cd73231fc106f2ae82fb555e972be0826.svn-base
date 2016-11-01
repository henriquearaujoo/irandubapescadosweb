package com.irandubamodulo01.bean;

import java.io.Serializable;
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

import com.irandubamodulo01.dao.TamanhoPeixeDAO;
import com.irandubamodulo01.model.TamanhoPeixe;
import com.irandubamodulo01.util.Filtro;

@Named(value = "tamanhoPeixeBean")
@ViewScoped
public class TamanhoPeixeBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject TamanhoPeixeDAO tamanhoPeixeDao;
	private @Inject TamanhoPeixe tamanhoPeixe;
	private Filtro filtro =  new Filtro();
	private LazyDataModel<TamanhoPeixe> model;
	

	
	public TamanhoPeixeBean(){
		
		model = new LazyDataModel<TamanhoPeixe>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<TamanhoPeixe> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				setRowCount(tamanhoPeixeDao.quantidadeFiltrados(filtro));
				return tamanhoPeixeDao.filtrados(filtro);
			}
		};
		
	}
	
	public void prepararCad(){
		tamanhoPeixe = new TamanhoPeixe();
	}

	public void salvar(){
	    try{
			tamanhoPeixeDao.save(tamanhoPeixe);
			tamanhoPeixe = new TamanhoPeixe();
			addMessage("Informação", "Tamanho salvo com sucesso.", FacesMessage.SEVERITY_INFO);
	    }catch(Exception e){
	    	addMessage("Erro", "Não foi possivel salvar o tamanho.", FacesMessage.SEVERITY_ERROR);
	    }
	} 
	
	public void delete(){

		try{
			tamanhoPeixeDao.remove(tamanhoPeixe);
			addMessage("Informação", "Tamanho excluído com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível excluir o tamanho.", FacesMessage.SEVERITY_ERROR);
		}
	}
	
	
	public void addMessage(String summary, String detail, Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	
	//Getters and Setters


	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public TamanhoPeixeDAO getTamanhoPeixeDao() {
		return tamanhoPeixeDao;
	}

	public void setTamanhoPeixeDao(TamanhoPeixeDAO tamanhoPeixeDao) {
		this.tamanhoPeixeDao = tamanhoPeixeDao;
	}

	public TamanhoPeixe getTamanhoPeixe() {
		return tamanhoPeixe;
	}

	public void setTamanhoPeixe(TamanhoPeixe tamanhoPeixe) {
		this.tamanhoPeixe = tamanhoPeixe;
	}

	public LazyDataModel<TamanhoPeixe> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<TamanhoPeixe> model) {
		this.model = model;
	}

	
	

	
}
