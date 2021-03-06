package com.irandubamodulo01.model;


import com.irandubamodulo01.enumerated.TipoPessoa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "fornecedor")
public class Fornecedor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column
	private String nome;

	@Column
	private String cpf;

	@Column
	private String cnpj;

	@Column
	private String rg;

	@Column
	private String nomeFantasia;

	@Column
	private String endereco;

	@Column
	private String complemento;

	@Column
	private String numero;

	@Column
	private String cep;

	@Column
	private String estado;

	@Column
	private String cidade;

	@Column
	private String bairro;

	@Column
	private String telefone1;

	@Column
	private String telefone2;

	@Column(columnDefinition = "TEXT")
	private String pontoReferencia;

	@Enumerated(EnumType.STRING)
	private TipoPessoa tipoPessoa;
	
	@OneToMany(mappedBy="fornecedor", fetch=FetchType.LAZY)
	private List<Compra> compras = new ArrayList<Compra>();
	
	@OneToMany(mappedBy = "fornecedor", fetch=FetchType.LAZY)
	private List<Barco> barcos = new ArrayList<Barco>();

	@OneToMany(mappedBy = "fornecedor", fetch=FetchType.LAZY)
	private List<AdiantamentoFornecedor> adiantamentos = new ArrayList<AdiantamentoFornecedor>();
	
	@OneToMany(mappedBy = "fornecedor", fetch=FetchType.LAZY)
	private List<Conta> contas = new ArrayList<Conta>();

	@OneToMany(mappedBy = "fornecedor", fetch=FetchType.LAZY)
	private List<PrecoDiferenciado> precosDiferenciados = new ArrayList<PrecoDiferenciado>();

	public Fornecedor() {

	}

	public Fornecedor(Long id, String nome) {
		this.id = id;
		this.nome = nome;
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
	

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}



	public List<Compra> getCompras() {
		return compras;
	}



	public void setCompras(List<Compra> compras) {
		this.compras = compras;
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
		Fornecedor other = (Fornecedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



	public List<Barco> getBarcos() {
		return barcos;
	}



	public void setBarcos(List<Barco> barcos) {
		this.barcos = barcos;
	}



	public List<AdiantamentoFornecedor> getAdiantamentos() {
		return adiantamentos;
	}



	public void setAdiantamentos(List<AdiantamentoFornecedor> adiantamentos) {
		this.adiantamentos = adiantamentos;
	}



	public List<Conta> getContas() {
		return contas;
	}



	public void setContas(List<Conta> contas) {
		this.contas = contas;
	}


	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public String getPontoReferencia() {
		return pontoReferencia;
	}

	public void setPontoReferencia(String pontoReferencia) {
		this.pontoReferencia = pontoReferencia;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public List<PrecoDiferenciado> getPrecosDiferenciados() {
		return precosDiferenciados;
	}

	public void setPrecosDiferenciados(List<PrecoDiferenciado> precosDiferenciados) {
		this.precosDiferenciados = precosDiferenciados;
	}
}
