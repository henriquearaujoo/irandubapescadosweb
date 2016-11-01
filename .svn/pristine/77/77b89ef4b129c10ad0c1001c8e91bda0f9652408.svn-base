package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.view.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.irandubamodulo01.dao.HistoricoAlteracaoValorDao;
import com.irandubamodulo01.daoimpl.CompraDAOImpl;
import com.irandubamodulo01.daoimpl.HistoricoAlteracaoValorDaoImpl;
import com.irandubamodulo01.daoimpl.LoteDAOImpl;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.HistoricoAlteracaoValor;
import com.irandubamodulo01.model.Lote;
import com.irandubamodulo01.model.Usuario;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.FormatterUtil;
import com.irandubamodulo01.util.ReportUtil;


@Named(value="relatorioHistoricoAlteracaoValor")
@ViewScoped
public class RelatorioHistoricoAlteracaoValor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject Compra compra;
	private @Inject CompraDAOImpl compraDAO; 
	private @Inject HistoricoAlteracaoValorDaoImpl historicoDAO;
	private @Inject LoteDAOImpl loteDAO;
	private Filtro filtro = new Filtro();
	private LazyDataModel<Compra> model;
	private Integer filtroStatus = -1;
	
	
	public RelatorioHistoricoAlteracaoValor(){
		carregarCompras();
	}
	
	public void carregarCompras(){
	
		model = new LazyDataModel<Compra>() {
			private static final long serialVersionUID = 1L;
				@Override
				public List<Compra> load(int first, int pageSize, String sortField,
						SortOrder sortOrder, Map<String, Object> filters) {
					filtro.setPrimeiroRegistro(first);
					filtro.setQuantidadeRegistros(pageSize);
					filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
					filtro.setPropriedadeOrdenacao(sortField);
					if (filtroStatus == -1)
						filtro.setStatus(null);
					else if (filtroStatus == 1)
						filtro.setStatus(true);
					else
						filtro.setStatus(false);
					setRowCount(compraDAO.quantidadeFiltrados(filtro));
					return compraDAO.filtrados(filtro);
				}
				
		 	};
	}
	
	public void imprimirRelatorio(){
		
		List<HistoricoAlteracaoValor> lista = historicoDAO.getHistorico(compra);
		List<com.irandubamodulo01.model.RelatorioHistoricoAlteracaoValor> listaHistorico = new ArrayList<com.irandubamodulo01.model.RelatorioHistoricoAlteracaoValor>();
		String descricao = "";
		
		BigDecimal totalDescontos = BigDecimal.ZERO;
		BigDecimal totalAcrescimos = BigDecimal.ZERO;
		
		if (lista.size() > 0){
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String path = request.getSession().getServletContext().getRealPath( "/" );
			
			int cont = 1;
			
			for (HistoricoAlteracaoValor itemHistorico : lista) {
				
				descricao = "";
				
				com.irandubamodulo01.model.RelatorioHistoricoAlteracaoValor rel = new com.irandubamodulo01.model.RelatorioHistoricoAlteracaoValor();
				
				descricao += "Em " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(itemHistorico.getData());
				descricao += " o usuário " + itemHistorico.getUsuario().getNome();
				descricao += " efetuou um " + (itemHistorico.getTipo().equals("DESCONTO") ? "desconto" : "acréscimo");
				descricao += " de R$"	+ FormatterUtil.getValorFormatado(itemHistorico.getValor());
				
				if (itemHistorico.getLote() != null){
					if (itemHistorico.getPesoValorPeixe() == null)
						descricao += " no lote " + itemHistorico.getLote().getSequencia() != null ? itemHistorico.getLote().getSequencia() : "0" + ".";
					else
						descricao += " no valor do KG do peixe.";
				}
				else
					descricao += " na compra."; 
				
				rel.setSequencia(cont);
				rel.setDescricao(descricao);
				rel.setObservacao(itemHistorico.getObservacao() != null ? itemHistorico.getObservacao() : "Sem observação.");
				
				if (itemHistorico.getTipo().equals("DESCONTO"))
					totalDescontos = totalDescontos.add(itemHistorico.getValor());
				else
					totalAcrescimos = totalAcrescimos.add(itemHistorico.getValor());
				
				listaHistorico.add(rel);
				
				cont++;
				
				rel = null;
				
			}
			
			com.irandubamodulo01.model.RelatorioHistoricoAlteracaoValor rel = new com.irandubamodulo01.model.RelatorioHistoricoAlteracaoValor();
			rel.listaHistorico = listaHistorico;
			
			Map parametros = new HashMap();
			
			parametros.put("prCompra", compra.getCodigo() != null ? compra.getCodigo() : "Sem código");
			parametros.put("prFornecedor", compra.getFornecedor() != null ? compra.getFornecedor().getNome() : "Sem fornecedor");
			parametros.put("prBarco", compra.getBarco() != null ? compra.getBarco().getNome() : "Sem barco");
			parametros.put("prTotalDescontos", "R$ " + FormatterUtil.getValorFormatado(totalDescontos));
			parametros.put("prTotalAcrescimo", "R$ " + FormatterUtil.getValorFormatado(totalAcrescimos));
			
			JRDataSource dataSource = new JRBeanCollectionDataSource(rel.listaHistorico);
			
			try {
				JasperDesign jd = JRXmlLoader.load(path + "relatorios/relatorio_historico_compras.jrxml");
				JasperReport report = JasperCompileManager.compileReport(jd);
				ReportUtil.openReport("Relatório", "relatorio_historico_compras" + new SimpleDateFormat("hhmmssddMMyyyy").format(new Date()), report, parametros, dataSource);
				
				addMessage("Informação: ", "Relatório exportado com sucesso.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			rel = null;
			lista = null;
			listaHistorico = null;
		}
		else
			addMessageAviso("Aviso: ", "Não há dados a serem exportados no relatório.");
		
	}
	
	public void limparFiltro(){
		filtro.setNome("");
		filtro.setDataInicio(null);
		filtro.setDataFinal(null);
		filtroStatus = 0;
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

	public LazyDataModel<Compra> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Compra> model) {
		this.model = model;
	}
	
	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public Integer getFiltroStatus() {
		return filtroStatus;
	}

	public void setFiltroStatus(Integer filtroStatus) {
		this.filtroStatus = filtroStatus;
	}
	
}
