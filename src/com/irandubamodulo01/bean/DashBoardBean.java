package com.irandubamodulo01.bean;

import com.irandubamodulo01.dao.CamaraDAO;
import com.irandubamodulo01.dao.CompraDAO;
import com.irandubamodulo01.dao.PagamentoDAO;
import com.irandubamodulo01.dao.PosicaoCamaraDAO;
import com.irandubamodulo01.enumerated.StatusCompra;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.PosicaoCamara;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.FormatterUtil;
import com.irandubamodulo01.util.PeixePesoUtil;
import com.irandubamodulo01.util.ReportUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Named(value = "dashBoardBean")
@ViewScoped
public class DashBoardBean implements Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private @Inject
	CompraDAO compraDAO;
	private @Inject
	PagamentoDAO pagamentoDAO;

	private String tarefasUsuario = "";
	private String valorTotalCompras = "";
	private String valorTotalComprasPrimeiroPeriodo = "";
	private String valorTotalComprasSegundoPeriodo = "";
	private String pesoTotalCompras = "";
	private String pesoTotalComprasNaoFinalizadas = "";
	private String pesoTotalComprasPrimeiroPeriodo = "";
	private String pesoTotalComprasSegundoPeriodo = "";

	private Integer qtdeComprasAPagar = 0;
	private Integer qtdeComprasAAutorizar = 0;
	private Integer qtdePagamentosPendentes = 0;

	private List<PeixePesoUtil> peixesRanking = new ArrayList<PeixePesoUtil>();

	public DashBoardBean(){

	}

	public void obterTarefasUsuario(){
		if(getUsuarioSession().getUsuario().getPerfil().getDescricao().equals("Admin"))
		{
			Integer qtdeComprasAutorizar = compraDAO.obterQtdeComprasAAutorizar();

			if (qtdePagamentosPendentes != null && qtdePagamentosPendentes > 0)
				qtdeComprasAutorizar = qtdeComprasAutorizar + qtdePagamentosPendentes;

			tarefasUsuario = qtdeComprasAutorizar + " tarefas";
		}else {

			Integer qtdeComprasAPagar = compraDAO.obterQtdeComprasAPagar();

			tarefasUsuario = qtdeComprasAPagar + " tarefas";
		}
	}

	public void obterValoresCompra(){

		BigDecimal valorTotal = compraDAO.obterValorTotalComprasPorData(new Date());
		BigDecimal pesoTotal = compraDAO.obterPesoTotalComprasPorData(new Date());
		BigDecimal pesoTotalComprasNaoFinalizadas = compraDAO.obterPesoTotalComprasNaoFinalizadasPorData(new Date());

		Calendar calI = Calendar.getInstance();
		calI.setTime(new Date());

		Calendar calF = Calendar.getInstance();
		calF.setTime(new Date());

		calI.set(Calendar.HOUR_OF_DAY, 0);
		calI.set(Calendar.MINUTE, 1);
		calI.set(Calendar.SECOND, 0);

		calF.set(Calendar.HOUR_OF_DAY, 12);
		calF.set(Calendar.MINUTE, 0);
		calF.set(Calendar.SECOND, 59);

		BigDecimal valorTotalP = compraDAO.obterValorTotalComprasPorPeriodo(calI.getTime(), calF.getTime());
		BigDecimal pesoTotalP = compraDAO.obterPesoTotalComprasPorPeriodo(calI.getTime(), calF.getTime());

		calI.set(Calendar.HOUR_OF_DAY, 12);
		calI.set(Calendar.MINUTE, 1);
		calI.set(Calendar.SECOND, 0);

		calF.set(Calendar.HOUR_OF_DAY, 23);
		calF.set(Calendar.MINUTE, 59);
		calF.set(Calendar.SECOND, 59);

		BigDecimal valorTotalS = compraDAO.obterValorTotalComprasPorPeriodo(calI.getTime(), calF.getTime());
		BigDecimal pesoTotalS = compraDAO.obterPesoTotalComprasPorPeriodo(calI.getTime(), calF.getTime());

		valorTotalCompras = "R$ " + FormatterUtil.getValorFormatado(valorTotal);
		valorTotalComprasPrimeiroPeriodo = "R$ " + FormatterUtil.getValorFormatado(valorTotalP);
		valorTotalComprasSegundoPeriodo =  "R$ " + FormatterUtil.getValorFormatado(valorTotalS);

		pesoTotalCompras = FormatterUtil.getValorFormatado(pesoTotal) + " kg";
		this.pesoTotalComprasNaoFinalizadas = FormatterUtil.getValorFormatado(pesoTotalComprasNaoFinalizadas) + " kg";
		pesoTotalComprasPrimeiroPeriodo = FormatterUtil.getValorFormatado(pesoTotalP.toPlainString()) + " kg";
		pesoTotalComprasSegundoPeriodo =  FormatterUtil.getValorFormatado(pesoTotalS.toPlainString()) + " kg";

		qtdeComprasAPagar = compraDAO.obterQtdeComprasAPagar();

		qtdeComprasAAutorizar = compraDAO.obterQtdeComprasAAutorizar();

		peixesRanking = compraDAO.obterTop10TotalPeixesPesadosPorData(new Date());

	}

	public void obterPagamentosPendentes(){
		qtdePagamentosPendentes = pagamentoDAO.obterQtdePagamentosPendentes();
	}

	public void gerarNotificacoesEAlertas(){
		obterPagamentosPendentes();
		obterTarefasUsuario();
		obterValoresCompra();
	}

	public UsuarioSessionBean getUsuarioSession(){
		UsuarioSessionBean usuarioSession = (UsuarioSessionBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuariologado");
		return usuarioSession;
	}

	public void imprimirRelPesagensDia() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");

		Map parametros = new HashMap();

		String periodo = "Dia " + new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		parametros.put("periodo", periodo);

		try {
			JasperDesign jd = null;

			String nomeRel = "relatorio_pesagens_dia_";
			jd = JRXmlLoader.load(path + "relatorios/relatorio_pesagens_dia.jrxml");

			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReportCon("Relatório", nomeRel + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()), report, parametros, compraDAO.getConnection());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void imprimirRelatorioCompras(){

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String path = request.getSession().getServletContext().getRealPath( "/" );

		Map parametros = new HashMap();

		String filtroConsulta = "1=1";

		String periodo = "--";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		periodo = "Dia " + sdf.format(new Date());
		filtroConsulta += " AND to_char(c.datacompra, 'dd/MM/yyyy') = '" + sdf.format(new Date()) + "'";

		filtroConsulta += " AND c.status = true AND c.pause = false and c.statuscompra = 'PAGO'";

		parametros.put("filtro_consulta", filtroConsulta);
		parametros.put("periodo", periodo);

		try {
			JasperDesign jd = null;

			String nomeRel = "";

			nomeRel = "relatorio_compras_";
			jd = JRXmlLoader.load(path + "relatorios/relatorio_compras.jrxml");

			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReportCon("Relatório", nomeRel + new SimpleDateFormat("HHmmssddMMyyyy").format(new Date()), report, parametros, compraDAO.getConnection());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String carregarComprasAutorizar(){
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("statusCompra", StatusCompra.AGUARDA_AUTORIZACAO);

		return "baixa_compra?faces-redirect=true";
	}

	public StatusCompra getStatusCompraAutorizar(){
		return StatusCompra.AGUARDA_AUTORIZACAO;
	}

	public String getTarefasUsuario() {
		return tarefasUsuario;
	}

	public void setTarefasUsuario(String tarefasUsuario) {
		this.tarefasUsuario = tarefasUsuario;
	}

	public String getValorTotalComprasSegundoPeriodo() {
		return valorTotalComprasSegundoPeriodo;
	}

	public void setValorTotalComprasSegundoPeriodo(String valorTotalComprasSegundoPeriodo) {
		this.valorTotalComprasSegundoPeriodo = valorTotalComprasSegundoPeriodo;
	}

	public String getValorTotalComprasPrimeiroPeriodo() {
		return valorTotalComprasPrimeiroPeriodo;
	}

	public void setValorTotalComprasPrimeiroPeriodo(String valorTotalComprasPrimeiroPeriodo) {
		this.valorTotalComprasPrimeiroPeriodo = valorTotalComprasPrimeiroPeriodo;
	}

	public String getValorTotalCompras() {
		return valorTotalCompras;
	}

	public void setValorTotalCompras(String valorTotalCompras) {
		this.valorTotalCompras = valorTotalCompras;
	}

	public String getPesoTotalCompras() {
		return pesoTotalCompras;
	}

	public void setPesoTotalCompras(String pesoTotalCompras) {
		this.pesoTotalCompras = pesoTotalCompras;
	}

	public String getPesoTotalComprasPrimeiroPeriodo() {
		return pesoTotalComprasPrimeiroPeriodo;
	}

	public void setPesoTotalComprasPrimeiroPeriodo(String pesoTotalComprasPrimeiroPeriodo) {
		this.pesoTotalComprasPrimeiroPeriodo = pesoTotalComprasPrimeiroPeriodo;
	}

	public String getPesoTotalComprasSegundoPeriodo() {
		return pesoTotalComprasSegundoPeriodo;
	}

	public void setPesoTotalComprasSegundoPeriodo(String pesoTotalComprasSegundoPeriodo) {
		this.pesoTotalComprasSegundoPeriodo = pesoTotalComprasSegundoPeriodo;
	}

	public Integer getQtdeComprasAPagar() {
		return qtdeComprasAPagar;
	}

	public void setQtdeComprasAPagar(Integer qtdeComprasAPagar) {
		this.qtdeComprasAPagar = qtdeComprasAPagar;
	}

	public Integer getQtdeComprasAAutorizar() {
		return qtdeComprasAAutorizar;
	}

	public void setQtdeComprasAAutorizar(Integer qtdeComprasAAutorizar) {
		this.qtdeComprasAAutorizar = qtdeComprasAAutorizar;
	}

	public Integer getQtdePagamentosPendentes() {
		return qtdePagamentosPendentes;
	}

	public void setQtdePagamentosPendentes(Integer qtdePagamentosPendentes) {
		this.qtdePagamentosPendentes = qtdePagamentosPendentes;
	}

	public String getPesoTotalComprasNaoFinalizadas() {
		return pesoTotalComprasNaoFinalizadas;
	}

	public void setPesoTotalComprasNaoFinalizadas(String pesoTotalComprasNaoFinalizadas) {
		this.pesoTotalComprasNaoFinalizadas = pesoTotalComprasNaoFinalizadas;
	}

	public List<PeixePesoUtil> getPeixesRanking() {
		return peixesRanking;
	}

	public void setPeixesRanking(List<PeixePesoUtil> peixesRanking) {
		this.peixesRanking = peixesRanking;
	}
}
