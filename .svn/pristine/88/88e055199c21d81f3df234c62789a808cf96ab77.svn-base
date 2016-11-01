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
import com.irandubamodulo01.dao.EmbalagemDAO;
import com.irandubamodulo01.dao.PosicaoCamaraDAO;
import com.irandubamodulo01.dao.TipoPeixeDAO;
import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.Embalagem;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.PosicaoCamara;
import com.irandubamodulo01.model.TipoPeixe;
import com.irandubamodulo01.util.Filtro;

@Named(value = "embalagemBean")
@ViewScoped
public class EmbalagemBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject EmbalagemDAO embalagemDao;
	private @Inject Embalagem embalagem;
	private Filtro filtro =  new Filtro();
	private LazyDataModel<Embalagem> model;
	

	
	public EmbalagemBean(){
		
		model = new LazyDataModel<Embalagem>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<Embalagem> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				setRowCount(embalagemDao.quantidadeFiltrados(filtro));
				return embalagemDao.filtrados(filtro);
			}
		};
		
	}
	
	public void prepararCad(){
		embalagem = new Embalagem();
	}

	public void salvar(){
	    try{
			embalagemDao.save(embalagem);
			embalagem = new Embalagem();
			addMessage("Informação", "Embalagem salva com sucesso.", FacesMessage.SEVERITY_INFO);
	    }catch(Exception e){
	    	addMessage("Erro", "Não foi possivel salvar a embalagem", FacesMessage.SEVERITY_ERROR);
	    }
	} 
	
	public void delete(){
		try{
			embalagemDao.remove(embalagem);
			addMessage("Informação", "Embalagem excluida com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possivel excluir a embalagem.", FacesMessage.SEVERITY_ERROR);
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

	public EmbalagemDAO getEmbalagemDao() {
		return embalagemDao;
	}

	public void setEmbalagemDao(EmbalagemDAO embalagemDao) {
		this.embalagemDao = embalagemDao;
	}

	public Embalagem getEmbalagem() {
		return embalagem;
	}

	public void setEmbalagem(Embalagem embalagem) {
		this.embalagem = embalagem;
	}

	public LazyDataModel<Embalagem> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Embalagem> model) {
		this.model = model;
	}
	
}
