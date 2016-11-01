package com.irandubamodulo01.util;

import java.io.Serializable;
import java.util.Date;

import com.irandubamodulo01.enumerated.StatusArmazenamento;
import com.irandubamodulo01.enumerated.StatusCompra;
import com.irandubamodulo01.enumerated.StatusPedido;
import com.irandubamodulo01.model.TipoPagamento;
import com.irandubamodulo01.model.Usuario;


public class Filtro implements Serializable{

	private String codigo;
	private String descricao;
	private Date data;
	private String nome;
    private Date dataInicio;
    private Date dataFinal;
    private int primeiroRegistro;
    private int quantidadeRegistros;
    private String propriedadeOrdenacao;
    private boolean ascendente;
    private Boolean status;
    private Boolean pause;
    private Boolean autorizado;
    private StatusCompra statusCompra;
    private TipoPagamento tipoPagamento;
    private Boolean pendente;
    private Usuario usuario;
    private StatusArmazenamento filtroStatus;
    private String statusRetirada;
	private StatusPedido statusPedido;
    
    public Filtro(){}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public Date getData() {
		return data;
	}


	public void setData(Date data) {
		this.data = data;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public Date getDataInicio() {
		return dataInicio;
	}


	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}


	public Date getDataFinal() {
		return dataFinal;
	}


	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}


	public int getPrimeiroRegistro() {
		return primeiroRegistro;
	}


	public void setPrimeiroRegistro(int primeiroRegistro) {
		this.primeiroRegistro = primeiroRegistro;
	}


	public int getQuantidadeRegistros() {
		return quantidadeRegistros;
	}


	public void setQuantidadeRegistros(int quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}


	public String getPropriedadeOrdenacao() {
		return propriedadeOrdenacao;
	}


	public void setPropriedadeOrdenacao(String propriedadeOrdenacao) {
		this.propriedadeOrdenacao = propriedadeOrdenacao;
	}


	public boolean isAscendente() {
		return ascendente;
	}


	public void setAscendente(boolean ascendente) {
		this.ascendente = ascendente;
	}


	public Boolean getStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	public Boolean getPause() {
		return pause;
	}


	public void setPause(Boolean pause) {
		this.pause = pause;
	}


	public Boolean getAutorizado() {
		return autorizado;
	}


	public void setAutorizado(Boolean autorizado) {
		this.autorizado = autorizado;
	}


	public StatusCompra getStatusCompra() {
		return statusCompra;
	}


	public void setStatusCompra(StatusCompra statusCompra) {
		this.statusCompra = statusCompra;
	}


	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}


	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}


	public Boolean getPendente() {
		return pendente;
	}


	public void setPendente(Boolean pendente) {
		this.pendente = pendente;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public StatusArmazenamento getFiltroStatus() {
		return filtroStatus;
	}


	public void setFiltroStatus(StatusArmazenamento filtroStatus) {
		this.filtroStatus = filtroStatus;
	}


	public String getStatusRetirada() {
		return statusRetirada;
	}


	public void setStatusRetirada(String statusRetirada) {
		this.statusRetirada = statusRetirada;
	}


	public StatusPedido getStatusPedido() {
		return statusPedido;
	}

	public void setStatusPedido(StatusPedido statusPedido) {
		this.statusPedido = statusPedido;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}
