package com.irandubamodulo01.model;

import java.io.Serializable;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

import com.irandubamodulo01.bean.UsuarioSessionBean;

@Entity
public class Observacao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(columnDefinition="TEXT")
	private String observacao;
	@Column
	private Date data;
	@ManyToOne
	private Compra compra;
	@ManyToOne
	private Usuario usuario;

	public Observacao(){
		this.observacao = "";
		this.compra = new Compra();
		this.usuario = new Usuario();
		UsuarioSessionBean usuarioSession = (UsuarioSessionBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuariologado");
		this.usuario = usuarioSession != null ? usuarioSession.getUsuario() : null;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getObservacao() {
		return observacao;
	}


	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	public Compra getCompra() {
		return compra;
	}


	public void setCompra(Compra compra) {
		this.compra = compra;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public Date getData() {
		return data;
	}


	public void setData(Date data) {
		this.data = data;
	}
	
}
