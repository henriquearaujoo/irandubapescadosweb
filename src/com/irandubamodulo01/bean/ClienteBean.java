package com.irandubamodulo01.bean;

import com.irandubamodulo01.dao.ClienteDAO;
import com.irandubamodulo01.enumerated.TipoPessoa;
import com.irandubamodulo01.model.Cliente;
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

@Named(value = "clienteBean")
@ViewScoped
public class ClienteBean implements Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private @Inject
	ClienteDAO clienteDAO;
	private @Inject
	Cliente cliente;
	private Filtro filtro =  new Filtro();
	private LazyDataModel<Cliente> model;
	private Boolean inclusaoContinua = true;

	public ClienteBean(){
		
		model = new LazyDataModel<Cliente>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<Cliente> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				setRowCount(clienteDAO.quantidadeFiltrados(filtro));
				return clienteDAO.filtrados(filtro);
			}
		};
		
	}

	public String salvar(){
	    try{
			setCliente(clienteDAO.save(getCliente()));
			addMessage("Informação", "Cliente salvo com sucesso.", FacesMessage.SEVERITY_INFO);
	    }catch(Exception e){
			e.printStackTrace();
	    	addMessage("Erro", "Não foi possível salvar o cliente.", FacesMessage.SEVERITY_ERROR);
	    }

		if (getInclusaoContinua()) {
			cliente = new Cliente();
			return "";
		}else
			return cancelar();
	} 
	
	public void delete(){
		try{
			clienteDAO.remove(getCliente());
			addMessage("Informação", "Cliente excluído com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível excluir o cliente..", FacesMessage.SEVERITY_ERROR);
		}

	}

	public String prepararTelaCad(){
		return  "clientecad?faces-redirect=true";
	}
	
	public String editar(){
		return "clientecad?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public String cancelar(){
		return "clientes?faces-redirect=true";
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

	public LazyDataModel<Cliente> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Cliente> model) {
		this.model = model;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Boolean getInclusaoContinua() {
		return inclusaoContinua;
	}

	public void setInclusaoContinua(Boolean inclusaoContinua) {
		this.inclusaoContinua = inclusaoContinua;
	}
}
