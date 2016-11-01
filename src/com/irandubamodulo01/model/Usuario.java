package com.irandubamodulo01.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="usuario")
public class Usuario implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(length = 120, nullable=false)
	private String nome;
	@Column(length = 30, nullable=false)
	private String login;
	@Column(length = 20, nullable = false)
	private String senha;
	@Column(length = 15)
	private String cpf;
	@Column(length = 20)
	private String rg;
	@Column
	private String imei;
	@Column
	private String sexo;
	@ManyToOne
	@JoinColumn(name="perfil_id")
	private Perfil perfil;
	@Column
	private Boolean enable;
	
	
	public Usuario(){
		this.nome = "";
		this.login = "";
		this.senha = "";
		this.rg = "";
		this.cpf = "";
		this.sexo = "";
		
	}
       

	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}





	public Long getId() {
		return id;
	}





	public void setId(Long id) {
		this.id = id;
	}





	public String getNome() {
		return nome;
	}





	public void setNome(String nome) {
		this.nome = nome;
	}





	public String getLogin() {
		return login;
	}





	public void setLogin(String login) {
		this.login = login;
	}





	public String getSenha() {
		return senha;
	}





	public void setSenha(String senha) {
		this.senha = senha;
	}





	public String getCpf() {
		return cpf;
	}





	public void setCpf(String cpf) {
		this.cpf = cpf;
	}





	public String getRg() {
		return rg;
	}





	public void setRg(String rg) {
		this.rg = rg;
	}





	public String getSexo() {
		return sexo;
	}





	public void setSexo(String sexo) {
		this.sexo = sexo;
	}





	public Perfil getPerfil() {
		return perfil;
	}





	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}





	public Boolean getEnable() {
		return enable;
	}





	public void setEnable(Boolean enable) {
		this.enable = enable;
	}





	public String getImei() {
		return imei;
	}





	public void setImei(String imei) {
		this.imei = imei;
	}
	
	
	
	
}
