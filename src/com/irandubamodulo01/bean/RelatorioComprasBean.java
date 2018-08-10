package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.faces.application.FacesMessage;
import javax.faces.view.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.irandubamodulo01.daoimpl.FornecedorDAOImpl;
import com.irandubamodulo01.model.Fornecedor;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import com.irandubamodulo01.daoimpl.CompraDAOImpl;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.ReportUtil;


@Named(value="relatorioComprasBean")
@ViewScoped
public class RelatorioComprasBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject CompraDAOImpl compraDAO;
	private @Inject FornecedorDAOImpl fornecedorDAO;
	private Filtro filtro = new Filtro();
	private List<Compra> listaCompras;
	private Integer filtroStatus = 1;
	private Integer tipoArquivo = 1;
	private String nomeFornecedor;
	private List<Fornecedor> fornecedores;
	private Boolean finalizadas;

	public RelatorioComprasBean(){
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

		if (filtroStatus.intValue() != 2) {
			if (filtro.getDataInicio() != null && filtro.getDataFinal() != null){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(filtro.getDataFinal());
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				periodo = "De " + sdf.format(filtro.getDataInicio()) + " até " + sdf.format(filtro.getDataFinal());
				filtroConsulta += " AND c.datacompra between to_timestamp('" + sdf.format(filtro.getDataInicio()) + "', 'dd/MM/yyyy') AND to_timestamp('" + sdf.format(calendar.getTime()) + "', 'dd/MM/yyyy')";
			}else if (filtro.getDataInicio() != null && filtro.getDataFinal() == null){
				periodo = "Dia " + sdf.format(filtro.getDataInicio());
				filtroConsulta += " AND to_char(c.datacompra, 'dd/MM/yyyy') = '" + sdf.format(filtro.getDataInicio()) + "'";
			}else if (filtro.getDataInicio() == null && filtro.getDataFinal() != null){
				periodo = "Dia " + sdf.format(filtro.getDataInicio());
				filtroConsulta += " AND to_char(c.datacompra, 'dd/MM/yyyy') = '" + sdf.format(filtro.getDataFinal()) + "'";
			}

			if (nomeFornecedor != null && !nomeFornecedor.isEmpty()) {
				List<Fornecedor> fornecedor = getFornecedores().stream().filter(o -> o.getNome().equals(nomeFornecedor)).collect(Collectors.toList());
				if (filtroStatus.intValue() == 1 || filtroStatus.intValue() == 2)
					//filtroConsulta += " AND lower(f.nome) like '" + filtro.getNome().toLowerCase() + "%' ";
					filtroConsulta += " AND f.id = " + fornecedor.get(0).getId();
				else
					//filtroConsulta += " AND l.fornecedor_id in (select id from fornecedor where lower(nome) like '" + filtro.getNome().toLowerCase() + "%') ";
					filtroConsulta += " AND l.fornecedor_id = " + fornecedor.get(0).getId();
			}

			if (finalizadas)
				filtroConsulta += " AND c.status = true AND c.pause = false and (c.statuscompra = 'PAGO' or c.statuscompra = 'FINALIZADA_PAGAMENTO_PENDENTE')";
		}
		else{
			if (filtro.getDataInicio() != null && filtro.getDataFinal() != null){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(filtro.getDataFinal());
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				periodo = "De " + sdf.format(filtro.getDataInicio()) + " até " + sdf.format(filtro.getDataFinal());
				filtroConsulta += " AND c.datacompra between to_timestamp('" + sdf.format(filtro.getDataInicio()) + "', 'dd/MM/yyyy') AND to_timestamp('" + sdf.format(calendar.getTime()) + "', 'dd/MM/yyyy')";
			}else if (filtro.getDataInicio() != null && filtro.getDataFinal() == null){
				periodo = "Dia " + sdf.format(filtro.getDataInicio());
				filtroConsulta += " AND to_char(c.datacompra, 'dd/MM/yyyy') = '" + sdf.format(filtro.getDataInicio()) + "'";
			}else if (filtro.getDataInicio() == null && filtro.getDataFinal() != null){
				periodo = "Dia " + sdf.format(filtro.getDataInicio());
				filtroConsulta += " AND to_char(c.datacompra, 'dd/MM/yyyy') = '" + sdf.format(filtro.getDataFinal()) + "'";
			}

			if (nomeFornecedor != null && !nomeFornecedor.isEmpty()) {
				List<Fornecedor> fornecedor = getFornecedores().stream().filter(o -> o.getNome().equals(nomeFornecedor)).collect(Collectors.toList());
				if (filtroStatus.intValue() == 1 || filtroStatus.intValue() == 2)
					//filtroConsulta += " AND lower(f.nome) like '" + filtro.getNome().toLowerCase() + "%' ";
					filtroConsulta += " AND f.id = " + fornecedor.get(0).getId();
				else
					//filtroConsulta += " AND l.fornecedor_id in (select id from fornecedor where lower(nome) like '" + filtro.getNome().toLowerCase() + "%') ";
					filtroConsulta += " AND l.fornecedor_id = " + fornecedor.get(0).getId();
			}

			filtroConsulta += " AND c.status = true AND c.pause = false ";
			//filtroConsulta += " AND obs.observacao like 'Foi inserido o número da NF de entrada. Número ' || c.notafiscal || '.%'";

		}

		parametros.put("filtro_consulta", filtroConsulta);
		parametros.put("periodo", periodo);

		try {
			JasperDesign jd = null;

			String nomeRel = "";

			if (filtroStatus.intValue() == 1) {
				nomeRel = "relatorio_compras_";
				if (tipoArquivo == 1)
					jd = JRXmlLoader.load(path + "relatorios/relatorio_compras.jrxml");
				else
					jd = JRXmlLoader.load(path + "relatorios/relatorio_compras_xls.jrxml");
			}else if (filtroStatus.intValue() == 2) {
				nomeRel = "relatorio_compras_por_fornecedor_";
				if (tipoArquivo == 1)
					jd = JRXmlLoader.load(path + "relatorios/relatorio_compras_por_fornecedor.jrxml");
				else
					jd = JRXmlLoader.load(path + "relatorios/relatorio_compras_por_fornecedor_xls.jrxml");
			}else {
				nomeRel = "relatorio_compras_por_produto_";
				if (tipoArquivo == 1)
					jd = JRXmlLoader.load(path + "relatorios/relatorio_compras_por_peixe.jrxml");
				else
					jd = JRXmlLoader.load(path + "relatorios/relatorio_compras_por_peixe_xls.jrxml");
			}

			JasperReport report = JasperCompileManager.compileReport(jd);
			if (tipoArquivo == 1)
				ReportUtil.openReportCon("Relatório", nomeRel + new SimpleDateFormat("HHmmssddMMyyyy").format(new Date()), report, parametros, compraDAO.getConnection(), "PDF");
			else
				ReportUtil.openReportCon("Relatório", nomeRel + new SimpleDateFormat("HHmmssddMMyyyy").format(new Date()), report, parametros, compraDAO.getConnection(), "XLS");

			addMessage("Informação: ", "Relatório exportado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<String> completeFornecedor(String query){
		List<String> results = new ArrayList<String>();
		for (Fornecedor fornecedor : getFornecedores()){
			if (fornecedor.getNome().toLowerCase().startsWith(query.toLowerCase()))
				results.add(fornecedor.getNome());
		}
		return results;
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
	
	public String prepararTelaCad(){
		return "baixa_compras?faces-redirect=true";
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

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public List<Fornecedor> getFornecedores() {
		if (fornecedores == null)
			fornecedores = fornecedorDAO.getFornecedoresComplete();

		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public Boolean getFinalizadas() {
		return finalizadas;
	}

	public void setFinalizadas(Boolean finalizadas) {
		this.finalizadas = finalizadas;
	}

	public Integer getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(Integer tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}
	
	
}
