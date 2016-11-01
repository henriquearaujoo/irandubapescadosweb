package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.irandubamodulo01.dao.PerfilDAO;
import com.irandubamodulo01.dao.UsuarioDAO;
import com.irandubamodulo01.model.Perfil;
import com.irandubamodulo01.model.Usuario;
import com.irandubamodulo01.util.Filtro;

@Named(value = "usuariobean")
@javax.faces.view.ViewScoped
public class UsuarioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private @Inject Usuario usuario;
	private @Inject UsuarioDAO usuDAO;
	private @Inject PerfilDAO perfilDAO;
	private Filtro filtro = new Filtro();
	private LazyDataModel<Usuario> model;
	private List<SelectItem> perfis;
	private Boolean inclusaoContinua = true;

	public UsuarioBean() {
		model = new LazyDataModel<Usuario>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<Usuario> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				setRowCount(usuDAO.quantidadeFiltrados(filtro));
				return usuDAO.filtrados(filtro);
			}

		};

	}

	public String cancelar() {
		return "usuarios?faces-redirect=true";

	}

	public String salvarUsuario() {
		try{
			usuDAO.save(usuario);
			addMessage("Informação", "Usuario salvo com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível salvar o usuário.", FacesMessage.SEVERITY_ERROR);
		}

		if (getInclusaoContinua()) {
			usuario = new Usuario();
			return "";
		}else
			return cancelar();
	}

	public void deleteUsuario() {
		try{
			usuDAO.remove(usuario);
			addMessage("Informação", "Usuario excluído com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível excluir o usuário.", FacesMessage.SEVERITY_ERROR);
		}

	}

	public String editarUsuario() {
		return "usuarioscad?faces-redirect=true&amp;includeViewParams=true";
	}

	/*
	 * public void filtrarUsuario(){ listaUsuarios = new ArrayList<Usuario>();
	 * listaUsuarios = usuDAO.filtrar(filtro.getNome()); }
	 */

	public String prepararTelaCad() {
		return "usuarioscad?faces-redirect=true";
	}
	
	public void carregarPerfis(){
		List<Perfil> perfis = perfilDAO.getPerfis();
		
		this.perfis = new ArrayList<SelectItem>();
		
		for (Perfil perfil : perfis) {
			this.perfis.add(new SelectItem(perfil, perfil.getDescricao()));
		}
	}

	public void addMessage(String summary, String detail, FacesMessage.Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public LazyDataModel<Usuario> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Usuario> model) {
		this.model = model;
	}

	public List<SelectItem> getPerfis() {
		carregarPerfis();
		return perfis;
	}

	public void setPerfis(List<SelectItem> perfis) {
		this.perfis = perfis;
	}

	public Boolean getInclusaoContinua() {
		return inclusaoContinua;
	}

	public void setInclusaoContinua(Boolean inclusaoContinua) {
		this.inclusaoContinua = inclusaoContinua;
	}
}
