package com.irandubamodulo01.bean;

import com.irandubamodulo01.dao.TransportadoraDAO;
import com.irandubamodulo01.dao.VendedorDAO;
import com.irandubamodulo01.enumerated.TipoPessoa;
import com.irandubamodulo01.model.Transportadora;
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

@Named(value = "transportadoraBean")
@ViewScoped
public class TransportadoraBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private @Inject
	TransportadoraDAO transportadoraDAO;
	private @Inject
	Transportadora transportadora;
	private Filtro filtro =  new Filtro();
	private LazyDataModel<Transportadora> model;
	private Boolean inclusaoContinua = true;

	public TransportadoraBean(){
		
		model = new LazyDataModel<Transportadora>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<Transportadora> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				setRowCount(transportadoraDAO.quantidadeFiltrados(filtro));
				return transportadoraDAO.filtrados(filtro);
			}
		};
		
	}

	public String salvar(){
	    try{
			setTransportadora(transportadoraDAO.save(getTransportadora()));
			addMessage("Informação", "Transportadora salva com sucesso.", FacesMessage.SEVERITY_INFO);
	    }catch(Exception e){
	    	addMessage("Erro", "Não foi possível salvar a transportadora.", FacesMessage.SEVERITY_ERROR);
	    }

		if (inclusaoContinua) {
			transportadora = new Transportadora();
			return "";
		}else
			return cancelar();
	} 
	
	public void delete(){
		try{
			transportadoraDAO.remove(getTransportadora());
			addMessage("Informação", "Transportadora excluída com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível excluir a transportadora.", FacesMessage.SEVERITY_ERROR);
		}

	}

	public String prepararTelaCad(){
		return  "transportadoracad?faces-redirect=true";
	}
	
	public String editar(){
		return "transportadoracad?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public String cancelar(){
		return "transportadoras?faces-redirect=true";
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

	public LazyDataModel<Transportadora> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Transportadora> model) {
		this.model = model;
	}

	public Transportadora getTransportadora() {
		return transportadora;
	}

	public void setTransportadora(Transportadora transportadora) {
		this.transportadora = transportadora;
	}

	public Boolean getInclusaoContinua() {
		return inclusaoContinua;
	}

	public void setInclusaoContinua(Boolean inclusaoContinua) {
		this.inclusaoContinua = inclusaoContinua;
	}
}
