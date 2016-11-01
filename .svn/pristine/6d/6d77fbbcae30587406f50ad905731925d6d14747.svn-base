package com.irandubamodulo01.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.irandubamodulo01.service.ComponentFather;

@Entity
@Table(name="lote")
public class Lote implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final BigDecimal PESO_CACAPA = new BigDecimal(2);
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Long id;
	@ManyToOne
	@JoinColumn(name="peixe_id")
	private Peixe peixe;
	@ManyToOne
	@JoinColumn(name="fornecedor_id")
	private Fornecedor fornecedor;
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="compra_id")
	private Compra compra;
	@Column
	private BigDecimal peso;
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal valor;
	@Column
	private Integer qtdCaixas;
	@Column
	private BigDecimal valorUnitarioPeixe;
	@Column
	private Integer sequencia;
	@Column
	private BigDecimal descontokg;
	@Column
	private BigDecimal desconto;
	@Column
	private BigDecimal acrescimo;
	@Column
	private BigDecimal pesoCacapa;
	@Column
	private Boolean isPrecoDiferenciado;

	@Transient
	private String codigo;

	@Transient
	private BigDecimal totalValorKG;

	@Transient
	private BigDecimal totalPesoBruto;

	@Transient
	private BigDecimal totalPesoLiquido;

	@Transient
	private Integer totalCaixas;

	@Transient
	private BigDecimal totalValor;

	@Transient
	private Boolean nohPai;

	@Transient
	private BigDecimal totalDesconto;

	@Transient
	private BigDecimal totalAcrescimo;

	@Transient
	private BigDecimal totalValorTabela;


	@Transient
	private BigDecimal pesoLiquido;

	public Lote(){}
	
	public Lote(Long id,
			BigDecimal valorKg, 
			BigDecimal pesoBruto,  
			Integer qtdCaixas, 
			BigDecimal pesoCacapa,
			Long idPeixe,
			String nomePeixe, BigDecimal valorTotal, BigDecimal acrescimo, BigDecimal desconto){
		
		this.id = id;
		this.valorUnitarioPeixe = valorKg;
		this.peso = pesoBruto;
		this.qtdCaixas = qtdCaixas;
		this.pesoCacapa = pesoCacapa;
		this.peixe = new Peixe();
		this.peixe.setId(idPeixe);
		this.peixe.setDescricao(nomePeixe);
		this.valor = valorTotal;
		this.acrescimo = acrescimo;
		this.desconto = desconto;
		
		
	}

	public Lote(Long id,
				BigDecimal valorKg,
				BigDecimal pesoBruto,
				Integer qtdCaixas,
				BigDecimal pesoCacapa,
				Long idPeixe,
				String nomePeixe, BigDecimal valorPeixe, BigDecimal valorTotal, BigDecimal acrescimo, BigDecimal desconto){

		this.id = id;
		this.valorUnitarioPeixe = valorKg;
		this.peso = pesoBruto;
		this.qtdCaixas = qtdCaixas;
		this.pesoCacapa = pesoCacapa;
		this.peixe = new Peixe();
		this.peixe.setId(idPeixe);
		this.peixe.setDescricao(nomePeixe);
		this.peixe.setValor(valorPeixe);
		this.valor = valorTotal;
		this.acrescimo = acrescimo;
		this.desconto = desconto;
	}

	public Lote(Long id,
				BigDecimal valorKg,
				BigDecimal pesoBruto,
				Integer qtdCaixas,
				BigDecimal pesoCacapa,
				Long idPeixe,
				String nomePeixe, BigDecimal valorPeixe, BigDecimal valorTotal, BigDecimal acrescimo, BigDecimal desconto, Boolean isPrecoDiferenciado){

		this.id = id;
		this.valorUnitarioPeixe = valorKg;
		this.peso = pesoBruto;
		this.qtdCaixas = qtdCaixas;
		this.pesoCacapa = pesoCacapa;
		this.peixe = new Peixe();
		this.peixe.setId(idPeixe);
		this.peixe.setDescricao(nomePeixe);
		this.peixe.setValor(valorPeixe);
		this.valor = valorTotal;
		this.acrescimo = acrescimo;
		this.desconto = desconto;
		this.isPrecoDiferenciado = isPrecoDiferenciado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Peixe getPeixe() {
		return peixe;
	}

	public void setPeixe(Peixe peixe) {
		this.peixe = peixe;
	}

	
	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public Integer getQtdCaixas() {
		return qtdCaixas;
	}

	public void setQtdCaixas(Integer qtdCaixas) {
		this.qtdCaixas = qtdCaixas;
	}


	public Compra getCompra() {
		return compra;
	}


	public void setCompra(Compra compra) {
		this.compra = compra;
	}
	
	public BigDecimal getPesoLiquido() {
		if (peso != null && qtdCaixas != null){
			this.pesoLiquido = new BigDecimal(String.valueOf(peso.subtract(pesoCacapa.multiply(new BigDecimal(qtdCaixas))))); 
			return pesoLiquido;
		}else
			return pesoLiquido;
	}


	public void setPesoLiquido(BigDecimal pesoLiquido) {
		this.pesoLiquido = pesoLiquido;
	}

	public BigDecimal getValorUnitarioPeixe() {
		return valorUnitarioPeixe;
	}


	public void setValorUnitarioPeixe(BigDecimal valorUnitarioPeixe) {
		this.valorUnitarioPeixe = valorUnitarioPeixe;
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
		Lote other = (Lote) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public BigDecimal getDesconto() {
		return desconto;
	}


	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}


	public BigDecimal getAcrescimo() {
		return acrescimo;
	}


	public void setAcrescimo(BigDecimal acrescimo) {
		this.acrescimo = acrescimo;
	}


	public Integer getSequencia() {
		return sequencia;
	}


	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}


	public BigDecimal getDescontokg() {
		return descontokg;
	}


	public void setDescontokg(BigDecimal descontokg) {
		this.descontokg = descontokg;
	}


	public BigDecimal getPesoCacapa() {
		return pesoCacapa;
	}


	public void setPesoCacapa(BigDecimal pesoCacapa) {
		this.pesoCacapa = pesoCacapa;
	}

	public BigDecimal getValorSemAcrescimoEDesconto(){
		if (getPesoLiquido() != null && getValorUnitarioPeixe() != null) {
			BigDecimal acres = acrescimo != null ? acrescimo : BigDecimal.ZERO;
			BigDecimal desc = desconto != null ? desconto : BigDecimal.ZERO;
			return getValor().subtract(acres).add(desc);
		}else
			return getValor();
	}

	public BigDecimal getTotalValorKG() {
		return totalValorKG;
	}

	public void setTotalValorKG(BigDecimal totalValorKG) {
		this.totalValorKG = totalValorKG;
	}

	public BigDecimal getTotalPesoBruto() {
		return totalPesoBruto;
	}

	public void setTotalPesoBruto(BigDecimal totalPesoBruto) {
		this.totalPesoBruto = totalPesoBruto;
	}

	public BigDecimal getTotalPesoLiquido() {
		return totalPesoLiquido;
	}

	public void setTotalPesoLiquido(BigDecimal totalPesoLiquido) {
		this.totalPesoLiquido = totalPesoLiquido;
	}

	public Integer getTotalCaixas() {
		return totalCaixas;
	}

	public void setTotalCaixas(Integer totalCaixas) {
		this.totalCaixas = totalCaixas;
	}

	public BigDecimal getTotalValor() {
		return totalValor;
	}

	public void setTotalValor(BigDecimal totalValor) {
		this.totalValor = totalValor;
	}

	public Boolean getNohPai() {
		return nohPai;
	}

	public void setNohPai(Boolean nohPai) {
		this.nohPai = nohPai;
	}

	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	public BigDecimal getTotalAcrescimo() {
		return totalAcrescimo;
	}

	public void setTotalAcrescimo(BigDecimal totalAcrescimo) {
		this.totalAcrescimo = totalAcrescimo;
	}

	public BigDecimal getTotalValorTabela() {
		return totalValorTabela;
	}

	public void setTotalValorTabela(BigDecimal totalValorTabela) {
		this.totalValorTabela = totalValorTabela;
	}

	public Boolean getIsPrecoDiferenciado() {
		return isPrecoDiferenciado;
	}

	public void setIsPrecoDiferenciado(Boolean isPrecoDiferenciado) {
		this.isPrecoDiferenciado = isPrecoDiferenciado;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}
