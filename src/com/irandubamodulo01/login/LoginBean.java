package com.irandubamodulo01.login;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.irandubamodulo01.bean.UsuarioSessionBean;
import com.irandubamodulo01.dao.CompraDAO;
import com.irandubamodulo01.dao.UltimoLoginDAO;
import com.irandubamodulo01.dao.UsuarioDAO;
import com.irandubamodulo01.model.UltimoLogin;
import com.irandubamodulo01.model.Usuario;

@Named(value="loginBean")
@RequestScoped
public class LoginBean implements Serializable{

	public LoginBean(){}
	@Inject
	private UsuarioDAO usuDAO;
	@Inject
	private UltimoLoginDAO ultimoLoginDAO;
	@Inject
	private Usuario usuario;

	private UsuarioSessionBean usuSession;

	private String nomeUsuario;
	private String senhaUsuario;
	private String emailUsuario;

	private String senha;
	private String confirnacaoSenha;

	
	public String logar(){
		FacesContext context = FacesContext.getCurrentInstance();
		usuario =  usuDAO.logar(nomeUsuario, senhaUsuario);

		if (usuario != null) {
			usuSession = new UsuarioSessionBean();
			usuSession.setNome(usuario.getNome());
			usuSession.setDataLogin(new Date());
			usuSession.setLogado(true);
			usuSession.setUsuario(usuario);
			UltimoLogin ultimoLogin = ultimoLoginDAO.getUltimoLoginUsuario(usuario);
			usuSession.setUltimoLogin(ultimoLogin);
			adicionaUsuarioNaSession(usuSession);
			if (ultimoLogin != null) {
				ultimoLogin.setData(new Date());
				ultimoLogin.setUsuario(usuario);

				ultimoLoginDAO.save(ultimoLogin);
				return "/main/index.xhtml?faces-redirect=true";
			}else {
				return "/main/configuracao_senha.xhtml?faces-redirect=true";
			}
		}else{
			FacesMessage message = new FacesMessage("Usuario/Senha inválidos");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
		}
		
		return null;
		
	}

	public void adicionaUsuarioNaSession(UsuarioSessionBean usuarioSession){
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuariologado", usuarioSession);
	}

	public UsuarioSessionBean getUsuarioSession(){
		UsuarioSessionBean usuarioSession = (UsuarioSessionBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuariologado");
		return usuarioSession;
	}
	
	public String logout(){
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/main/login.xhtml?faces-redirect=true";
	}

	public String atualizarSenha(){

		FacesContext context = FacesContext.getCurrentInstance();

		try {

			if(senha.equals(confirnacaoSenha)) {
				usuSession = getUsuarioSession();

				usuario = usuSession.getUsuario();
				usuario.setSenha(senha);
				usuDAO.save(usuario);

				UltimoLogin ultimoLogin = ultimoLoginDAO.getUltimoLoginUsuario(usuario);
				if (ultimoLogin == null)
					ultimoLogin = new UltimoLogin();
				ultimoLogin.setData(new Date());
				ultimoLogin.setUsuario(usuario);

				usuSession.setUltimoLogin(ultimoLogin);
				adicionaUsuarioNaSession(usuSession);
				ultimoLoginDAO.save(ultimoLogin);

				senha = null;
				confirnacaoSenha = null;

				FacesMessage message = new FacesMessage("Senha atualizada com sucesso.");
				message.setSeverity(FacesMessage.SEVERITY_INFO);
				context.addMessage(null, message);

				return "/main/index.xhtml?faces-redirect=true";
			}else{
				FacesMessage message = new FacesMessage("As senhas devem ser iguais.");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				context.addMessage(null, message);
				return null;
			}
		}catch (Exception e){
			e.printStackTrace();

			FacesMessage message = new FacesMessage("Não foi possivel atualizar a senha.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}

	}
	

	
	public String getNomeUsuario() {
		return nomeUsuario;
	}


	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}


	public String getSenhaUsuario() {
		return senhaUsuario;
	}


	public void setSenhaUsuario(String senhaUsuario) {
		this.senhaUsuario = senhaUsuario;
	}


	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public String getConfirnacaoSenha() {
		return confirnacaoSenha;
	}

	public void setConfirnacaoSenha(String confirnacaoSenha) {
		this.confirnacaoSenha = confirnacaoSenha;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
