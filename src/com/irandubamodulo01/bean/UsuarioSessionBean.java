package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.irandubamodulo01.model.UltimoLogin;
import com.irandubamodulo01.model.Usuario;

public class UsuarioSessionBean implements Serializable{
	  
	private String nome;
	private Date dataLogin;
	private boolean logado;
	private Usuario usuario;
	private UltimoLogin ultimoLogin;
	
	public UsuarioSessionBean() {
	
	}

	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public Date getDataLogin() {
		return dataLogin;
	}



	public void setDataLogin(Date dataLogin) {
		this.dataLogin = dataLogin;
	}



	public boolean isLogado() {
		return logado;
	}



	public void setLogado(boolean logado) {
		this.logado = logado;
	}



	public boolean verificaLogado(){
		return nome != null;
	}


	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UltimoLogin getUltimoLogin() {
		return ultimoLogin;
	}

	public void setUltimoLogin(UltimoLogin ultimoLogin) {
		this.ultimoLogin = ultimoLogin;
	}
}
