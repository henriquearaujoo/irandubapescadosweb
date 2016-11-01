package com.irandubamodulo01.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "pagamento")
public class Pagamento implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	@Column
	private BigDecimal valor;
	@ManyToOne
	private Compra compra;
	@Enumerated(EnumType.STRING)
	private TipoPagamento tipoPagamento; 
	@Column
	private Boolean autorizado;
	@ManyToOne
	private Conta conta;
	@Column
	private Boolean pendente;
	@Column
	private Date dataPagamento;
	@Column
	private String nomeCheque;
	@Column
	private String numeroCheque;
	@ManyToOne
	private AdiantamentoFornecedor adiantamento;

	@Transient
	private String tipoDoPagamento;

	@Transient
	private Integer seq;

	@Transient
	private String statusPagamento;

	@Transient
	private Boolean novo;

	public Pagamento(){}

	public Pagamento(Long id,  BigDecimal valor) {
		this.id = id;
		this.valor = valor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	
	public Boolean getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(Boolean autorizado) {
		this.autorizado = autorizado;
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
		Pagamento other = (Pagamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public Boolean getPendente() {
		return pendente;
	}

	public void setPendente(Boolean pendente) {
		this.pendente = pendente;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
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

	public AdiantamentoFornecedor getAdiantamento() {
		return adiantamento;
	}

	public void setAdiantamento(AdiantamentoFornecedor adiantamento) {
		this.adiantamento = adiantamento;
	}

	public String getTipoDoPagamento() {
		return tipoDoPagamento;
	}

	public void setTipoDoPagamento(String tipoDoPagamento) {
		this.tipoDoPagamento = tipoDoPagamento;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(String statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	public Boolean getNovo() {
		return novo;
	}

	public void setNovo(Boolean novo) {
		this.novo = novo;
	}
}
