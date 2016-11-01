package com.irandubamodulo01.bean;

import com.irandubamodulo01.dao.VendedorDAO;
import com.irandubamodulo01.enumerated.TipoPessoa;
import com.irandubamodulo01.model.Vendedor;
import com.irandubamodulo01.util.Filtro;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named(value = "vendedorBean")
@ViewScoped
public class VendedorBean implements Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private @Inject
	VendedorDAO vendedorDAO;
	private @Inject Vendedor vendedor;
	private Filtro filtro =  new Filtro();
	private LazyDataModel<Vendedor> model;
	private Boolean inclusaoContinua = true;

	public VendedorBean(){
		
		model = new LazyDataModel<Vendedor>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<Vendedor> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				setRowCount(vendedorDAO.quantidadeFiltrados(filtro));
				return vendedorDAO.filtrados(filtro);
			}
		};
		
	}

	public String salvar(){
	    try{
			setVendedor(vendedorDAO.save(getVendedor()));
			addMessage("Informação", "Vendedor salvo com sucesso.", FacesMessage.SEVERITY_INFO);
	    }catch(Exception e){
	    	addMessage("Erro", "Não foi possível salvar o vendedor.", FacesMessage.SEVERITY_ERROR);
	    }

		if (getInclusaoContinua()) {
			vendedor = new Vendedor();
			return "";
		}else
			return cancelar();
	} 
	
	public void delete(){
		try {
			vendedorDAO.remove(getVendedor());
			addMessage("Informação", "Vendedor excluído com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível excluir o vendedor.", FacesMessage.SEVERITY_ERROR);
		}

	}

	public String prepararTelaCad(){
		return  "vendedorcad?faces-redirect=true";
	}
	
	public String editar(){
		return "vendedorcad?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public String cancelar(){
		return "vendedores?faces-redirect=true";
	}

	public void addMessage(String summary, String detail, Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	public TipoPessoa[] getTiposPessoa(){
		return TipoPessoa.values();
	}
	
	//Getters and Setters
	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public LazyDataModel<Vendedor> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Vendedor> model) {
		this.model = model;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public Boolean getInclusaoContinua() {
		return inclusaoContinua;
	}

	public void setInclusaoContinua(Boolean inclusaoContinua) {
		this.inclusaoContinua = inclusaoContinua;
	}
}
