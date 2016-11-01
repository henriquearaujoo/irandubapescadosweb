package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.view.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.irandubamodulo01.dao.FornecedorDAO;
import com.irandubamodulo01.dao.PeixeDAO;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.Peixe;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.irandubamodulo01.dao.CompraDAO;
import com.irandubamodulo01.dao.ConfiguracoesDAO;
import com.irandubamodulo01.daoimpl.CompraDAOImpl;
import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Configuracoes;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.ReportUtil;
import org.primefaces.event.SelectEvent;


@Named(value="relatorioComprasDetalhadasBean")
@ViewScoped
public class RelatorioComprasDetalhadasBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private @Inject CompraDAO compraDAO;
	private @Inject ConfiguracoesDAO configuracoesDAO;
	private @Inject
	FornecedorDAO fornecedorDAO;
	private @Inject
	PeixeDAO peixeDAO;
	private Filtro filtro = new Filtro();
	private Boolean detalhado = false;

	private String nomeFornecedor;
	private String nomePeixe;
	private List<Fornecedor> fornecedores;
	private List<Peixe> peixes;
	private String codigo;

	public RelatorioComprasDetalhadasBean(){

	}

	public void carregarListas(){
		setFornecedores(fornecedorDAO.getFornecedoresComplete());
		setPeixes(peixeDAO.getPeixesComplete());
	}

	public List<String> completeFornecedores(String query){
		List<String> results = new ArrayList<String>();
		for(Fornecedor fornecedor : getFornecedores()){
			if (fornecedor.getNome().toLowerCase().startsWith(query.toLowerCase()))
				results.add(fornecedor.getNome());
		}
		return results;
	}

	public List<String> completePeixes(String query){
		List<String> results = new ArrayList<String>();
		for(Peixe peixe : getPeixes()){
			if (peixe.getDescricao().toLowerCase().startsWith(query.toLowerCase()))
				results.add(peixe.getDescricao());
		}
		return results;
	}

	public void selecionaFornecedor(SelectEvent event){
		filtro.setNome(event.getObject().toString());
	}

	public void selecionaPeixe(SelectEvent event){
		filtro.setDescricao(event.getObject().toString());
	}
	
	@SuppressWarnings("unchecked")
	public void imprimir(){
				
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String path = request.getSession().getServletContext().getRealPath( "/" );
		
		Map parametros = new HashMap();

		Fornecedor fornecedor = null;
		Peixe peixe = null;

		if (filtro.getNome() != null && !filtro.getNome().isEmpty() && nomeFornecedor != null && !nomeFornecedor.isEmpty())
			fornecedor = fornecedorDAO.getFornecedorPorNome(filtro.getNome());

		if (filtro.getDescricao() != null && !filtro.getDescricao().isEmpty() && nomePeixe != null && !nomePeixe.isEmpty())
			peixe = peixeDAO.getPeixePorDescricao(filtro.getDescricao());
		
		String filtroConsulta = "1=1"; 
		filtroConsulta += fornecedor != null ? " AND f.id = " +  fornecedor.getId().longValue() + "" : "";
		filtroConsulta += peixe != null ? " AND p.id = " + peixe.getId().longValue() + "" : "";
		filtroConsulta += codigo != null && !codigo.isEmpty() ? " AND c.codigo = '" + codigo + "'" : "";
		
		String periodo = "--";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(filtro.getDataFinal());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			periodo = sdf.format(filtro.getDataInicio()) + " até " + sdf.format(filtro.getDataFinal());
			filtroConsulta += " AND c.datacompra between to_timestamp('" + sdf.format(filtro.getDataInicio()) + "', 'dd/MM/yyyy') AND to_timestamp('" + sdf.format(calendar.getTime()) + "', 'dd/MM/yyyy')"; 
		}else if (filtro.getDataInicio() != null && filtro.getDataFinal() == null){
			periodo = "Dia " + sdf.format(filtro.getDataInicio());
			filtroConsulta += " AND to_char(c.datacompra, 'dd/MM/yyyy') = '" + sdf.format(filtro.getDataInicio()) + "'";
		}else if (filtro.getDataInicio() == null && filtro.getDataFinal() != null){
			periodo = "Dia " + sdf.format(filtro.getDataInicio());
			filtroConsulta += " AND to_char(c.datacompra, 'dd/MM/yyyy') = '" + sdf.format(filtro.getDataFinal()) + "'";
		}
		
		List<Configuracoes> list = configuracoesDAO.getConfiguracoes();
		Configuracoes conf = list != null && list.size() > 0 ? list.get(0) : null;
		
		parametros.put("periodo", periodo);
		parametros.put("filtro_consulta", filtroConsulta);
		parametros.put("peso_cacapa", conf.getPesoCacapa().doubleValue());
		
		try {
			JasperDesign jd = null;
			if (detalhado)
				jd = JRXmlLoader.load(path + "relatorios/relatorio_compra_peixe.jrxml");
			else
				jd = JRXmlLoader.load(path + "relatorios/relatorio_compra_peixe_agrupado.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReportCon("Relatório", "relatorio_compra_detalhada" + new SimpleDateFormat("HHmmssddMMyyyy").format(new Date()), report, parametros, compraDAO.getConnection());
			
			addMessage("Informação: ", "Relatório exportado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void limparFiltro(){
		filtro.setNome("");
		filtro.setDescricao("");
		nomeFornecedor = "";
		nomePeixe = "";
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

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public Boolean getDetalhado() {
		return detalhado;
	}

	public void setDetalhado(Boolean detalhado) {
		this.detalhado = detalhado;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public String getNomePeixe() {
		return nomePeixe;
	}

	public void setNomePeixe(String nomePeixe) {
		this.nomePeixe = nomePeixe;
	}

	public List<Peixe> getPeixes() {
		return peixes;
	}

	public void setPeixes(List<Peixe> peixes) {
		this.peixes = peixes;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}
