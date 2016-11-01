package com.irandubamodulo01.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.irandubamodulo01.enumerated.StatusArmazenamento;

@Entity
@Table(name="armazenamento")
public class Armazenamento implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=true)
	private Long id;

	@Column
	private BigDecimal peso;

	@Column
	private Integer qtdeEmbalagem;

	@Column
	private Date data;

	@Enumerated(EnumType.STRING)
	private StatusArmazenamento status;

	@ManyToOne
	@JoinColumn(name="tipopeixe_id")
	private TipoPeixe tipoPeixe;

	@ManyToOne
	@JoinColumn(name="tamanhopeixe_id")
	private TamanhoPeixe tamanhoPeixe;

	@ManyToOne
	@JoinColumn(name="embalagem_id")
	private Embalagem embalagem;

	@ManyToOne
	@JoinColumn(name="camara_id")
	private Camara camara;

	@ManyToOne
	@JoinColumn(name="posicaocamara_id")
	private PosicaoCamara posicaoCamara;

	@ManyToOne
	@JoinColumn(name="peixe_id")
	private Peixe peixe;

	@ManyToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;

	@OneToMany(mappedBy = "armazenamento", fetch = FetchType.LAZY)
	private List<ObservacaoArmazenamento> observacoes;


	public Armazenamento(Long id, Camara camara, Lote lote,
			BigDecimal peso, String unidadeMedida) {
		super();
		this.id = id;
		this.camara = camara;
		this.peso = peso;
	}
	
	public Armazenamento(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ObservacaoArmazenamento> getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(List<ObservacaoArmazenamento> observacoes) {
		this.observacoes = observacoes;
	}

	public Camara getCamara() {
		return camara;
	}

	public void setCamara(Camara camara) {
		this.camara = camara;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}


	public StatusArmazenamento getStatus() {
		return status;
	}

	public void setStatus(StatusArmazenamento status) {
		this.status = status;
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
		Armazenamento other = (Armazenamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoPeixe getTipoPeixe() {
		return tipoPeixe;
	}

	public void setTipoPeixe(TipoPeixe tipoPeixe) {
		this.tipoPeixe = tipoPeixe;
	}

	public TamanhoPeixe getTamanhoPeixe() {
		return tamanhoPeixe;
	}

	public void setTamanhoPeixe(TamanhoPeixe tamanhoPeixe) {
		this.tamanhoPeixe = tamanhoPeixe;
	}

	public PosicaoCamara getPosicaoCamara() {
		return posicaoCamara;
	}

	public void setPosicaoCamara(PosicaoCamara posicaoCamara) {
		this.posicaoCamara = posicaoCamara;
	}

	public Peixe getPeixe() {
		return peixe;
	}

	public void setPeixe(Peixe peixe) {
		this.peixe = peixe;
	}

	public Embalagem getEmbalagem() {
		return embalagem;
	}

	public void setEmbalagem(Embalagem embalagem) {
		this.embalagem = embalagem;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getQtdeEmbalagem() {
		return qtdeEmbalagem;
	}

	public void setQtdeEmbalagem(Integer qtdeEmbalagem) {
		this.qtdeEmbalagem = qtdeEmbalagem;
	}
}
