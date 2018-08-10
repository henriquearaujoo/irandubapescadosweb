package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.view.*;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.mapping.Array;
import org.jboss.logging.annotations.Pos;

import com.irandubamodulo01.daoimpl.LoteDAOImpl;
import com.irandubamodulo01.daoimpl.PedidoDaoImpl;
import com.irandubamodulo01.daoimpl.ProdutoDaoImpl;
import com.irandubamodulo01.daoimpl.RastreabilidadeDAOImpl;
import com.irandubamodulo01.daoimpl.RetiradaDAOImpl;
import com.irandubamodulo01.enumerated.Posto;
import com.irandubamodulo01.model.Armazenamento;
import com.irandubamodulo01.model.Lote;
import com.irandubamodulo01.model.Produto;
import com.irandubamodulo01.model.Rastreabilidade;
import com.irandubamodulo01.model.Retirada;
import com.irandubamodulo01.daoimpl.ArmazenamentoDAOImpl;
import com.irandubamodulo01.daoimpl.CompraDAOImpl;
import com.irandubamodulo01.util.Filtro;


@Named(value="rastreabilidadeBean")
@ViewScoped
public class RastreabilidadeBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject RastreabilidadeDAOImpl rastreabilidadeDAO;
	private @Inject CompraDAOImpl compraDAO;
	private @Inject LoteDAOImpl loteDAO;
	private @Inject ArmazenamentoDAOImpl armDAO;
	private @Inject RetiradaDAOImpl retDAO;
	private @Inject ProdutoDaoImpl prodDAO;
	private Filtro filtro = new Filtro();
	private List<Lote> lotesRecebimentoFluvial;
	private BigDecimal pesoTotalRecebimentoFluvial = BigDecimal.ZERO;
	private List<Lote> lotesRecebimentoTerrestre;
	private BigDecimal pesoTotalRecebimentoTerrestre = BigDecimal.ZERO;
	private List<Armazenamento> armazenamentosTunel;
	private List<Retirada> retiradasTunel;
	private BigDecimal pesoTotalArmTunel = BigDecimal.ZERO;
	private BigDecimal pesoTotalRetTunel = BigDecimal.ZERO;
	private List<Armazenamento> armazenamentosEstocagem1;
	private List<Armazenamento> armazenamentosEstocagem2;
	private BigDecimal pesoTotalArmEstoc1 = BigDecimal.ZERO;
	private BigDecimal pesoTotalArmEstoc2 = BigDecimal.ZERO;
	private List<Retirada> retiradasEstocagem1;
	private List<Retirada> retiradasEstocagem2;
	private BigDecimal pesoTotalRetEstoic1 = BigDecimal.ZERO;
	private BigDecimal pesoTotalRetEstoic2 = BigDecimal.ZERO;
	private List<Produto> produtosExped1;
	private List<Produto> produtosExped2;
	private BigDecimal pesoTotalProdExped1 = BigDecimal.ZERO;
	private BigDecimal pesoTotalProdExped2 = BigDecimal.ZERO;
	private BigDecimal pesoTotalTunel = BigDecimal.ZERO;;
	private BigDecimal pesoTotalEstoc1 = BigDecimal.ZERO;;
	private BigDecimal pesoTotalEstoc2 = BigDecimal.ZERO;;
	
	public RastreabilidadeBean(){
	}
	
	public void carregarConfiguracao(){
		filtro.setDataInicio(new Date());
		filtro.setDataFinal(new Date());
		lotesRecebimentoFluvial = new ArrayList<>();
	}

	public void carregarItens(){
		
		Calendar calInicio = Calendar.getInstance();
		calInicio.setTime(filtro.getDataInicio());
		calInicio.set(Calendar.HOUR_OF_DAY, 0);
		calInicio.set(Calendar.MINUTE, 0);
		calInicio.set(Calendar.SECOND, 0);
		
		Calendar calFim = Calendar.getInstance();
		calFim.setTime(filtro.getDataFinal());
		calFim.set(Calendar.HOUR_OF_DAY, 23);
		calFim.set(Calendar.MINUTE, 59);
		calFim.set(Calendar.SECOND, 59);
		
		List<Rastreabilidade> rastrebilidades = rastreabilidadeDAO.getRastreabilidadesPorPeriodo(calInicio.getTime(), calFim.getTime());
		
		lotesRecebimentoFluvial = new ArrayList<>();
		lotesRecebimentoTerrestre = new ArrayList<>();
		armazenamentosEstocagem1 = new ArrayList<>();
		armazenamentosEstocagem2 = new ArrayList<>();
		retiradasEstocagem1 = new ArrayList<>();
		retiradasEstocagem2 = new ArrayList<>();
		armazenamentosTunel = new ArrayList<>();
		retiradasTunel = new ArrayList<>();
		produtosExped1 = new ArrayList<>();
		produtosExped2 = new ArrayList<>();
		
		pesoTotalRecebimentoFluvial = BigDecimal.ZERO;
		pesoTotalRecebimentoTerrestre = BigDecimal.ZERO;
		pesoTotalArmTunel = BigDecimal.ZERO;
		pesoTotalRetTunel = BigDecimal.ZERO;
		pesoTotalArmEstoc1 = BigDecimal.ZERO;
		pesoTotalArmEstoc2 = BigDecimal.ZERO;
		pesoTotalRetEstoic1 = BigDecimal.ZERO;
		pesoTotalRetEstoic2 = BigDecimal.ZERO;
		pesoTotalProdExped1 = BigDecimal.ZERO;
		pesoTotalProdExped2 = BigDecimal.ZERO;
		pesoTotalTunel = BigDecimal.ZERO;
		pesoTotalEstoc1 = BigDecimal.ZERO;
		pesoTotalEstoc2 = BigDecimal.ZERO;
		
		for (Rastreabilidade rastreabilidade : rastrebilidades) {
		
			if (rastreabilidade.getPosto().equals(Posto.RECEBIMENTO_FLUVIAL.toString())){
				if (rastreabilidade.getCompra() != null){
					lotesRecebimentoFluvial.addAll(loteDAO.buscarLotePorCompra(rastreabilidade.getCompra()));
				}
			}
			
			if (rastreabilidade.getPosto().equals(Posto.RECEBIMENTO_TERRESTRE.toString())){
				if (rastreabilidade.getCompra() != null){
					lotesRecebimentoTerrestre.addAll(loteDAO.buscarLotePorCompra(rastreabilidade.getCompra()));
				}
			}
			
			if (rastreabilidade.getPosto().equals(Posto.TUNEL.toString())){
				
				if (rastreabilidade.getArmazenamento() != null){
					armazenamentosTunel.add(armDAO.obterArmazenamentoPorId(rastreabilidade.getArmazenamento().getId()));
				}
				
				if (rastreabilidade.getRetirada() != null){
					retiradasTunel.add(retDAO.obterRetiradaPorId(rastreabilidade.getRetirada().getId()));
				}
			}
			
			if (rastreabilidade.getPosto().equals(Posto.ESTOCAGEM_1.toString())){
				if (rastreabilidade.getArmazenamento() != null){
					armazenamentosEstocagem1.add(armDAO.obterArmazenamentoPorId(rastreabilidade.getArmazenamento().getId()));
				}
				
				if (rastreabilidade.getRetirada() != null){
					retiradasEstocagem1.add(retDAO.obterRetiradaPorId(rastreabilidade.getRetirada().getId()));
				}
			}
			
			if (rastreabilidade.getPosto().equals(Posto.ESTOCAGEM_2.toString())){
				if (rastreabilidade.getArmazenamento() != null){
					armazenamentosEstocagem2.add(armDAO.obterArmazenamentoPorId(rastreabilidade.getArmazenamento().getId()));
				}
				
				if (rastreabilidade.getRetirada() != null){
					retiradasEstocagem2.add(retDAO.obterRetiradaPorId(rastreabilidade.getRetirada().getId()));
				}
			}

			if (rastreabilidade.getPosto().equals(Posto.EXPEDICAO_1.toString())){
				if (rastreabilidade.getPedido() != null){
					produtosExped1.addAll(prodDAO.getProdutosPorPedido(rastreabilidade.getPedido()));
				}
			}
			
			if (rastreabilidade.getPosto().equals(Posto.EXPEDICAO_2.toString())){
				if (rastreabilidade.getPedido() != null){
					produtosExped2.addAll(prodDAO.getProdutosPorPedido(rastreabilidade.getPedido()));
				}
			}
		}
		
		for (Lote lote : lotesRecebimentoTerrestre) {
			pesoTotalRecebimentoTerrestre = pesoTotalRecebimentoTerrestre.add(lote.getPesoLiquido());
		}
		
		for (Lote lote : lotesRecebimentoFluvial) {
			pesoTotalRecebimentoFluvial = pesoTotalRecebimentoFluvial.add(lote.getPesoLiquido());
		}
		
		for (Armazenamento arm : armazenamentosTunel) {
			pesoTotalArmTunel = pesoTotalArmTunel.add(arm.getPeso());
		}
		
		for (Retirada ret : retiradasTunel) {
			pesoTotalRetTunel = pesoTotalRetTunel.add(ret.getPeso());
		}
		
		for (Armazenamento arm : armazenamentosEstocagem1) {
			pesoTotalArmEstoc1 = pesoTotalArmEstoc1.add(arm.getPeso());
		}
		
		for (Retirada ret : retiradasEstocagem1) {
			pesoTotalRetEstoic1 = pesoTotalRetEstoic1.add(ret.getPeso());
		}
		
		for (Armazenamento arm : armazenamentosEstocagem2) {
			pesoTotalArmEstoc2 = pesoTotalArmEstoc2.add(arm.getPeso());
		}
		
		for (Retirada ret : retiradasEstocagem2) {
			pesoTotalRetEstoic2 = pesoTotalRetEstoic2.add(ret.getPeso());
		}
		
		for (Produto prod : produtosExped1) {
			pesoTotalProdExped1 = pesoTotalProdExped1.add(prod.getPeso());
		}
		
		for (Produto prod : produtosExped2) {
			pesoTotalProdExped2 = pesoTotalProdExped2.add(prod.getPeso());
		}
		
		pesoTotalTunel = pesoTotalArmTunel.subtract(pesoTotalRetTunel);
		pesoTotalEstoc1 = pesoTotalArmEstoc1.subtract(pesoTotalRetEstoic1);
		pesoTotalEstoc2 = pesoTotalArmEstoc2.subtract(pesoTotalRetEstoic2);
	}
	
	public void limparFiltro(){
		filtro.setNome("");
		filtro.setDataInicio(null);
		filtro.setDataFinal(null);
	}

	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public void addMessageAviso(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public String prepararTelaCad(){
		return "baixa_compras?faces-redirect=true";
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public List<Lote> getLotesRecebimentoFluvial() {
		return lotesRecebimentoFluvial;
	}

	public void setLotesRecebimentoFluvial(List<Lote> lotesRecebimentoFluvial) {
		this.lotesRecebimentoFluvial = lotesRecebimentoFluvial;
	}

	public BigDecimal getPesoTotalRecebimentoFluvial() {
		return pesoTotalRecebimentoFluvial;
	}

	public void setPesoTotalRecebimentoFluvial(BigDecimal pesoTotalRecebimentoFluvial) {
		this.pesoTotalRecebimentoFluvial = pesoTotalRecebimentoFluvial;
	}

	public List<Lote> getLotesRecebimentoTerrestre() {
		return lotesRecebimentoTerrestre;
	}

	public void setLotesRecebimentoTerrestre(List<Lote> lotesRecebimentoTerrestre) {
		this.lotesRecebimentoTerrestre = lotesRecebimentoTerrestre;
	}

	public BigDecimal getPesoTotalRecebimentoTerrestre() {
		return pesoTotalRecebimentoTerrestre;
	}

	public void setPesoTotalRecebimentoTerrestre(BigDecimal pesoTotalRecebimentoTerrestre) {
		this.pesoTotalRecebimentoTerrestre = pesoTotalRecebimentoTerrestre;
	}

	public List<Armazenamento> getArmazenamentosEstocagem1() {
		return armazenamentosEstocagem1;
	}

	public void setArmazenamentosEstocagem1(List<Armazenamento> armazenamentosEstocagem1) {
		this.armazenamentosEstocagem1 = armazenamentosEstocagem1;
	}

	public List<Armazenamento> getArmazenamentosEstocagem2() {
		return armazenamentosEstocagem2;
	}

	public void setArmazenamentosEstocagem2(List<Armazenamento> armazenamentosEstocagem2) {
		this.armazenamentosEstocagem2 = armazenamentosEstocagem2;
	}

	public BigDecimal getPesoTotalArmEstoc1() {
		return pesoTotalArmEstoc1;
	}

	public void setPesoTotalArmEstoc1(BigDecimal pesoTotalArmEstoc1) {
		this.pesoTotalArmEstoc1 = pesoTotalArmEstoc1;
	}

	public BigDecimal getPesoTotalArmEstoc2() {
		return pesoTotalArmEstoc2;
	}

	public void setPesoTotalArmEstoc2(BigDecimal pesoTotalArmEstoc2) {
		this.pesoTotalArmEstoc2 = pesoTotalArmEstoc2;
	}

	public List<Retirada> getRetiradasEstocagem1() {
		return retiradasEstocagem1;
	}

	public void setRetiradasEstocagem1(List<Retirada> retiradasEstocagem1) {
		this.retiradasEstocagem1 = retiradasEstocagem1;
	}

	public List<Retirada> getRetiradasEstocagem2() {
		return retiradasEstocagem2;
	}

	public void setRetiradasEstocagem2(List<Retirada> retiradasEstocagem2) {
		this.retiradasEstocagem2 = retiradasEstocagem2;
	}

	public BigDecimal getPesoTotalRetEstoic1() {
		return pesoTotalRetEstoic1;
	}

	public void setPesoTotalRetEstoic1(BigDecimal pesoTotalRetEstoic1) {
		this.pesoTotalRetEstoic1 = pesoTotalRetEstoic1;
	}

	public BigDecimal getPesoTotalRetEstoic2() {
		return pesoTotalRetEstoic2;
	}

	public void setPesoTotalRetEstoic2(BigDecimal pesoTotalRetEstoic2) {
		this.pesoTotalRetEstoic2 = pesoTotalRetEstoic2;
	}

	public List<Produto> getProdutosExped1() {
		return produtosExped1;
	}

	public void setProdutosExped1(List<Produto> produtosExped1) {
		this.produtosExped1 = produtosExped1;
	}

	public List<Produto> getProdutosExped2() {
		return produtosExped2;
	}

	public void setProdutosExped2(List<Produto> produtosExped2) {
		this.produtosExped2 = produtosExped2;
	}

	public BigDecimal getPesoTotalProdExped1() {
		return pesoTotalProdExped1;
	}

	public void setPesoTotalProdExped1(BigDecimal pesoTotalProdExped1) {
		this.pesoTotalProdExped1 = pesoTotalProdExped1;
	}

	public BigDecimal getPesoTotalProdExped2() {
		return pesoTotalProdExped2;
	}

	public void setPesoTotalProdExped2(BigDecimal pesoTotalProdExped2) {
		this.pesoTotalProdExped2 = pesoTotalProdExped2;
	}

	public List<Armazenamento> getArmazenamentosTunel() {
		return armazenamentosTunel;
	}

	public void setArmazenamentosTunel(List<Armazenamento> armazenamentosTunel) {
		this.armazenamentosTunel = armazenamentosTunel;
	}

	public List<Retirada> getRetiradasTunel() {
		return retiradasTunel;
	}

	public void setRetiradasTunel(List<Retirada> retiradasTunel) {
		this.retiradasTunel = retiradasTunel;
	}

	public BigDecimal getPesoTotalArmTunel() {
		return pesoTotalArmTunel;
	}

	public void setPesoTotalArmTunel(BigDecimal pesoTotalArmTunel) {
		this.pesoTotalArmTunel = pesoTotalArmTunel;
	}

	public BigDecimal getPesoTotalRetTunel() {
		return pesoTotalRetTunel;
	}

	public void setPesoTotalRetTunel(BigDecimal pesoTotalRetTunel) {
		this.pesoTotalRetTunel = pesoTotalRetTunel;
	}

	public BigDecimal getPesoTotalTunel() {
		return pesoTotalTunel;
	}

	public void setPesoTotalTunel(BigDecimal pesoTotalTunel) {
		this.pesoTotalTunel = pesoTotalTunel;
	}

	public BigDecimal getPesoTotalEstoc1() {
		return pesoTotalEstoc1;
	}

	public void setPesoTotalEstoc1(BigDecimal pesoTotalEstoc1) {
		this.pesoTotalEstoc1 = pesoTotalEstoc1;
	}

	public BigDecimal getPesoTotalEstoc2() {
		return pesoTotalEstoc2;
	}

	public void setPesoTotalEstoc2(BigDecimal pesoTotalEstoc2) {
		this.pesoTotalEstoc2 = pesoTotalEstoc2;
	}


}
