package com.irandubamodulo01.bean;

import com.irandubamodulo01.daoimpl.CompraDAOImpl;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.ReportUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;


@Named(value="relatorioPagamentosPescadoBean")
@ViewScoped
public class RelatorioPagamentosPescadoBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private @Inject CompraDAOImpl compraDAO; // = new CompraDAOImpl();
	private Filtro filtro = new Filtro();
	private List<Compra> listaCompras;
	private Integer filtroStatus = 1;

	public RelatorioPagamentosPescadoBean(){
	}

	@SuppressWarnings("unchecked")
	public void imprimir(){
		
		//carregarCompras();

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String path = request.getSession().getServletContext().getRealPath( "/" );

		Map parametros = new HashMap();

		String filtroConsulta = "1=1";

		String periodo = "--";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(filtro.getDataFinal());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			periodo = "De " + sdf.format(filtro.getDataInicio()) + " até " + sdf.format(filtro.getDataFinal());
			filtroConsulta += " AND p.datapagamento between to_timestamp('" + sdf.format(filtro.getDataInicio()) + "', 'dd/MM/yyyy') AND to_timestamp('" + sdf.format(calendar.getTime()) + "', 'dd/MM/yyyy')";
		}else if (filtro.getDataInicio() != null && filtro.getDataFinal() == null){
			periodo = "Dia " + sdf.format(filtro.getDataInicio());
			filtroConsulta += " AND to_char(p.datapagamento, 'dd/MM/yyyy') = '" + sdf.format(filtro.getDataInicio()) + "'";
		}else if (filtro.getDataInicio() == null && filtro.getDataFinal() != null){
			periodo = "Dia " + sdf.format(filtro.getDataInicio());
			filtroConsulta += " AND to_char(p.datapagamento, 'dd/MM/yyyy') = '" + sdf.format(filtro.getDataFinal()) + "'";
		}

		if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
			filtroConsulta += " AND lower(f.nome) like '" + filtro.getNome().toLowerCase() + "%' ";
		}

		//filtroConsulta += " AND c.status = true AND c.pause = false and c.statuscompra = 'PAGO'";

		parametros.put("filtro_consulta", filtroConsulta);
		parametros.put("periodo", periodo);

		try {
			JasperDesign jd = null;

			String nomeRel = "relatorio_compras_";
			jd = JRXmlLoader.load(path + "relatorios/relatorio_pagamentos_pescado.jrxml");

			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReportCon("Relatório", nomeRel + new SimpleDateFormat("HHmmssddMMyyyy").format(new Date()), report, parametros, compraDAO.getConnection(), "PDF");

			addMessage("Informação: ", "Relatório exportado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void limparFiltro(){
		filtro.setNome("");
		filtro.setDataInicio(null);
		filtro.setDataFinal(null);
		filtroStatus = -1;
	}

	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public void addMessageAviso(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public Integer getFiltroStatus() {
		return filtroStatus;
	}

	public void setFiltroStatus(Integer filtroStatus) {
		this.filtroStatus = filtroStatus;
	}

	public List<Compra> getListaCompras() {
		return listaCompras;
	}

	public void setListaCompras(List<Compra> listaCompras) {
		this.listaCompras = listaCompras;
	}

}
