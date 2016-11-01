package com.irandubamodulo01.model;

import com.irandubamodulo01.enumerated.TipoAdiantamento;
import com.irandubamodulo01.util.FormatterUtil;
import com.irandubamodulo01.util.Menuable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "adiantamento_fornecedor")
public class AdiantamentoFornecedor implements Menuable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String observacao;
	@ManyToOne
	@JoinColumn(name="fornecedor_id")
	private Fornecedor fornecedor;
	@Column
	private BigDecimal valor;
	@ManyToOne
	private Usuario  usuario;
	@Column
	private Boolean status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	@Column
	private String nomeCheque;
	@Column
	private String numeroCheque;

	@Enumerated(EnumType.STRING)
	private TipoAdiantamento tipo;
	@ManyToOne
	private Conta conta;
	
	public AdiantamentoFornecedor(){}

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

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getNomeCheque() {
		return nomeCheque;
	}

	public void setNomeCheque(String nomeCheque) {
		this.nomeCheque = nomeCheque;
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	public TipoAdiantamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoAdiantamento tipo) {
		this.tipo = tipo;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
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
		AdiantamentoFornecedor other = (AdiantamentoFornecedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String getLabel() {
		if (numeroCheque != null && nomeCheque != null)
			return nomeCheque + "/" + numeroCheque + " - R$ " + FormatterUtil.getValorFormatado(valor);
		return "";
	}

	@Override
	public Long getIdentifier() {
		return id;
	}
}
