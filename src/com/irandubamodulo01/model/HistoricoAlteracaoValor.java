package com.irandubamodulo01.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "historico_alteracao_valor")
public class HistoricoAlteracaoValor implements AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	@ManyToOne
	@JoinColumn(name = "compra_id")
	private Compra compra;
	@Column
	private BigDecimal valor;
	@Column 
	private String tipo;
	@ManyToOne
	@JoinColumn(name = "lote_id")
	private Lote  lote;
	@Column
	private BigDecimal pesoValorPeixe;
	@ManyToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;
	@Column
	private String observacao;
	@Column
	private BigDecimal ultimoValor;
	@Column
	private BigDecimal valorDaCompra;

	//ALTERACAOITEM, ALTERACAOKG, ALTERACAOCOMPRA
	@Column
	private String descricao;
		
	public HistoricoAlteracaoValor(){}
	
	public HistoricoAlteracaoValor(Compra compra){
		this.compra = compra;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getData() {
		return data;
	}
	 public void setData(Date data) {
		this.data = data;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
	}

	public BigDecimal getPesoValorPeixe() {
		return pesoValorPeixe;
	}

	public void setPesoValorPeixe(BigDecimal pesoValorPeixe) {
		this.pesoValorPeixe = pesoValorPeixe;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public BigDecimal getUltimoValor() {
		return ultimoValor;
	}

	public void setUltimoValor(BigDecimal ultimoValor) {
		this.ultimoValor = ultimoValor;
	}


	public BigDecimal getValorDaCompra() {
		return valorDaCompra;
	}

	public void setValorDaCompra(BigDecimal valorDaCompra) {
		this.valorDaCompra = valorDaCompra;
	}


	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
		HistoricoAlteracaoValor other = (HistoricoAlteracaoValor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
