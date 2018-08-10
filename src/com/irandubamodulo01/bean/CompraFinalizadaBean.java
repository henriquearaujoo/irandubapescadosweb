package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.model.SelectItem;
import javax.faces.view.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.irandubamodulo01.dao.*;
import com.irandubamodulo01.enumerated.StatusCompra;
import com.irandubamodulo01.enumerated.TipoAdiantamento;
import com.irandubamodulo01.enumerated.TipoHistoricoCompra;
import com.irandubamodulo01.model.*;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.FormatterUtil;
import com.irandubamodulo01.util.ReportUtil;

@Named(value = "compraFinalizadaBean")
@ViewScoped
public class CompraFinalizadaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private @Inject Compra compra;
	private @Inject CompraDAO compraDAO;
	private @Inject PagamentoDAO pagamentoDAO; 
	private @Inject HistoricoAlteracaoValorDao historicoDAO;
	private @Inject ObservacaoDAO observacaoDAO;
	private @Inject PagamentoDAO pagDao;
	private @Inject
	ConfiguracoesDAO configuracoesDAO;
	private @Inject ObservacaoDAO obsDao;
	
	private Filtro filtro = new Filtro();
	private LazyDataModel<Compra> model;
	
	//Detalhes da compra
	private Observacao observacao;
	
	private BigDecimal totalPesoBruto = BigDecimal.ZERO;
	private BigDecimal totalPesoLiquido = BigDecimal.ZERO;
	private Integer totalQtdeCacapa = 0;
	private BigDecimal totalCompra = BigDecimal.ZERO;
	
	private BigDecimal totalDescontos = BigDecimal.ZERO;
	private BigDecimal totalAcrescimos = BigDecimal.ZERO;
	
	private BigDecimal pagDinheiro;
	private BigDecimal pagTransferencia;
	private BigDecimal pagCheque;
	private BigDecimal pagAdiantamentoDinheiro;
	private BigDecimal pagAdiantamentoCheque;
	private BigDecimal pagAdiantamentoTransferencia;
	private BigDecimal totalPago;
	private BigDecimal faltaPagar;
	private BigDecimal totalAdiantamento;
	
	private String observacaoAdiantamentoZero;
	private String observacaoAcrescimoPeso;
	private String observacaoAcrescimoValor;
	private List<Pagamento> pagamentos;
	
	public CompraFinalizadaBean() {
		//usuario = UsuarioSessionBean.getUsuario();
		carregarCompras();
	}

	public void carregarCompras() {

		model = new LazyDataModel<Compra>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<Compra> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);

				filtro.setStatus(true);
				filtro.setPause(false);
				filtro.setAutorizado(true);
				
				setRowCount(compraDAO.quantidadeFiltrados(filtro));
				return compraDAO.filtrados(filtro);
			}

		};
	}

	public UsuarioSessionBean getUsuarioSession(){
		UsuarioSessionBean usuarioSession = (UsuarioSessionBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuariologado");
		return usuarioSession;
	}

	public void salvarCompra(){
		Observacao observacao = new Observacao();
		observacao.setCompra(compra);
		observacao.setUsuario(getUsuarioSession().getUsuario());
		observacao.setData(new Date());
		observacao.setObservacao("Foi inserido o número da NF de entrada. Número " + compra.getNotaFiscal() + ".");
		observacaoDAO.save(observacao);
		compraDAO.save(compra);
		addMessage("Informação", "Compra salva com sucesso.", FacesMessage.SEVERITY_INFO);
	}

	@SuppressWarnings("unchecked")
	public void imprimirRecibo() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		
		String tiposPagamento = "";
		BigDecimal pagDinheiro = BigDecimal.ZERO;
		List<Pagamento> list = pagamentoDAO.getPagamentoPorCompra(compra);
		for (Pagamento pagamento : list) {
			if (pagamento.getTipoPagamento().equals(TipoPagamento.DINHEIRO) )
				pagDinheiro = pagDinheiro.add(pagamento.getValor());
			else if (pagamento.getTipoPagamento().equals(TipoPagamento.ADIANTAMENTO)) {
				if (pagamento.getAdiantamento() != null && pagamento.getAdiantamento().getTipo().equals(TipoAdiantamento.CHEQUE) && pagamento.getAdiantamento().getNumeroCheque() != null && pagamento.getAdiantamento().getNomeCheque() != null)
					tiposPagamento += "R$ " + FormatterUtil.getValorFormatado(pagamento.getValor()) + " pago com adiantamento em cheque. Número: " + pagamento.getAdiantamento().getNumeroCheque() + "\n";
				else if (pagamento.getAdiantamento() != null && pagamento.getAdiantamento().getTipo().equals(TipoAdiantamento.TRANSFERENCIA) && pagamento.getAdiantamento().getConta() != null) {
					String tipoConta = pagamento.getAdiantamento().getConta().getTipo() == TipoConta.CORRENTE ? "CC" : "CP";
					tiposPagamento += "R$ " + FormatterUtil.getValorFormatado(pagamento.getValor()) + " pago com adiantamento via transferência. " + pagamento.getAdiantamento().getConta().getBanco() + " AG " + pagamento.getAdiantamento().getConta().getAgencia() +
							" " + tipoConta + " " + pagamento.getAdiantamento().getConta().getConta() +"\n";
				}else
					tiposPagamento += "R$ " + FormatterUtil.getValorFormatado(pagamento.getValor()) + " pago com adiantamento em dinheiro. \n";
			}else if (pagamento.getTipoPagamento().equals(TipoPagamento.CHEQUE))
				tiposPagamento += "R$ " + FormatterUtil.getValorFormatado(pagamento.getValor()) + " pago em cheque. Número: " + pagamento.getNumeroCheque()+ "\n";
			else{
				String tipoConta = pagamento.getConta().getTipo() == TipoConta.CORRENTE ? "CC" : "CP"; 
				tiposPagamento += "R$ " + FormatterUtil.getValorFormatado(pagamento.getValor()) + " pago via transferência. " + pagamento.getConta().getBanco() + " AG " + pagamento.getConta().getAgencia() +
				" " + tipoConta + " " + pagamento.getConta().getConta() +"\n";
			}
		}
		
		if (pagDinheiro.compareTo(BigDecimal.ZERO) == 1)
			tiposPagamento += "R$ " + FormatterUtil.getValorFormatado(pagDinheiro) + " pago em dinheiro. \n";

		Map parametros = new HashMap();

		parametros.put("parametroValor","R$ " + FormatterUtil.getValorFormatado(compra.getValorTotal()));
		parametros.put("parametroQuantiaExtenso", FormatterUtil
				.valorPorExtenso(compra.getValorTotal().doubleValue()));
		parametros.put("parametroPagamento", "Compra de pescados");
		parametros.put("parametroNome", compra.getFornecedor().getNome());
		// parametros.put("parametroPeriodo", "Per�odo de " +
		// getData(recibo.getInicio()) + " at� " + getData(recibo.getFim()));
		parametros.put("parametroCNPJ", "CNPJ: 04.408.929/0001-05");
		parametros.put("parametroEndereco", "Av. Beira Rio, s/n - Iranduba/AM");
		parametros.put("parametroCEP", "CEP: 69415-000");
		parametros.put("parametroEmpresa", "Iranduba Frigorífico de Pescados");
		parametros.put("parametroTiposPagamento", tiposPagamento);

		try {
			JasperDesign jd = JRXmlLoader
					.load(path + "relatorios/recibo.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport(
					"Recibo",
					"recibo_"
							+ new SimpleDateFormat("HHmmssddMMyyyy")
									.format(new Date()), report, parametros,
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void gerarHistoricoUsuarioCompra(TipoHistoricoCompra tipo){
		/*HistoricoUsuarioCompra histUsuCompra = new HistoricoUsuarioCompra();
		histUsuCompra.setCompra(compra);
		histUsuCompra.setUsuario(getUsuarioSession().getUsuario());
		histUsuCompra.setData(new Date());
		histUsuCompra.setTipo(tipo);
		hisUsuCompraDao.save(histUsuCompra);*/

		Observacao obs = new Observacao();
		obs.setCompra(compra);
		obs.setUsuario(getUsuarioSession().getUsuario());
		obs.setData(new Date());

		if (tipo.equals(TipoHistoricoCompra.AUTORIZOU))
			obs.setObservacao("Compra foi autorizada.");
		else if (tipo.equals(TipoHistoricoCompra.ENVIOU_AUTORIZACAO))
			obs.setObservacao("Compra enviada para autorização.");
		else if (tipo.equals(TipoHistoricoCompra.FINALIZOU))
			obs.setObservacao("Compra foi finalizada.");
		else if (tipo.equals(TipoHistoricoCompra.RETORNOU))
			obs.setObservacao("Compra retornou para correção.");
		else if (tipo.equals(TipoHistoricoCompra.RETORNOU_INICIO))
			obs.setObservacao("Compra retornou para o primeiro posto para correção.");
		else if (tipo.equals(TipoHistoricoCompra.REABRIU))
			obs.setObservacao("Compra foi reaberta para correção.");

		obsDao.save(obs);
	}

	public String reabrirCompra() {

		if (!observacao.getObservacao().equalsIgnoreCase("")) {
			observacao.setCompra(compra);
			observacao.setData(new Date());
			obsDao.save(observacao);
		}

		compra.setStatus(false);
		compra.setStatusCompra(StatusCompra.REABERTA);
		compra.setAutorizado(null);
		compra.setPause(false);
		compraDAO.save(compra);
		gerarHistoricoUsuarioCompra(TipoHistoricoCompra.REABRIU);
		carregarCompras();
		zerarTotais();

		addMessage("Informação: ", "Compra reaberta.",
				FacesMessage.SEVERITY_INFO);

		return cancelar();

	}

	public void imprimirEspelho(){

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String path = request.getSession().getServletContext().getRealPath( "/" );

		Map parametros = new HashMap();

		Fornecedor fornecedor = null;
		Peixe peixe = null;

		String filtroConsulta = "1=1";
		filtroConsulta += compra.getCodigo() != null && !compra.getCodigo().isEmpty() ? " AND c.codigo = '" + compra.getCodigo() + "'" : "";

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

			jd = JRXmlLoader.load(path + "relatorios/relatorio_compra_peixe.jrxml");

			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReportCon("Relatório", "relatorio_compra_detalhada" + new SimpleDateFormat("HHmmssddMMyyyy").format(new Date()), report, parametros, compraDAO.getConnection(), "PDF");

			addMessage("Informação: ", "Relatório exportado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void limparFiltro() {
		filtro.setNome("");
		filtro.setDataInicio(null);
		filtro.setDataFinal(null);
	}
	
	public String prepararDetalhesCompra(){
		return "detalhescomprafinalizada?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public void zerarTotais() {
		totalPesoBruto = BigDecimal.ZERO;
		totalPesoLiquido = BigDecimal.ZERO;
		totalQtdeCacapa = 0;

		totalDescontos = BigDecimal.ZERO;
		totalAcrescimos = BigDecimal.ZERO;
		totalCompra = BigDecimal.ZERO;

		pagDinheiro = BigDecimal.ZERO;
		pagAdiantamentoDinheiro = BigDecimal.ZERO;
		pagAdiantamentoCheque = BigDecimal.ZERO;
		pagAdiantamentoTransferencia = BigDecimal.ZERO;
		pagTransferencia = BigDecimal.ZERO;
		pagCheque = BigDecimal.ZERO;
		totalPago = BigDecimal.ZERO;
		faltaPagar = BigDecimal.ZERO;

	}
	
	public void carregarDetalhesCompra() {
		
		observacao = new Observacao();
		compra.setLotes(compraDAO.getListaLotesCompra(compra));
		List<HistoricoAlteracaoValor> listaDeHistorico = new ArrayList<HistoricoAlteracaoValor>();
		listaDeHistorico = historicoDAO.getHistoricoPorCompra(compra.getId()
				.longValue());

		zerarTotais();

		for (Lote lote : compra.getLotes()) {
			totalPesoBruto = lote.getPeso() != null ? totalPesoBruto.add(lote
					.getPeso()) : BigDecimal.ZERO;
			totalPesoLiquido = lote.getPesoLiquido() != null ? totalPesoLiquido
					.add(lote.getPesoLiquido()) : BigDecimal.ZERO;
			totalQtdeCacapa = lote.getQtdCaixas() != null ? (totalQtdeCacapa + lote
					.getQtdCaixas()) : 0;
			totalCompra = totalCompra.add(lote.getValor());

		}

		totalAcrescimos = BigDecimal.ZERO;
		totalDescontos = BigDecimal.ZERO;
		BigDecimal acrescimoEmLote = BigDecimal.ZERO;
		BigDecimal descontoEmLote = BigDecimal.ZERO;
		for (HistoricoAlteracaoValor historico : listaDeHistorico) {
			if (historico.getTipo().equalsIgnoreCase("ACRESCIMO")) {
				totalAcrescimos = totalAcrescimos.add(historico.getValor());
				if (historico.getLote() != null) {
					acrescimoEmLote = acrescimoEmLote.add(historico.getValor());
				}

			} else {
				totalDescontos = totalDescontos.add(historico.getValor());
				if (historico.getLote() != null) {
					descontoEmLote = descontoEmLote.add(historico.getValor());
				}
			}

		}

		totalCompra = totalCompra.add(totalAcrescimos).add(descontoEmLote)
				.subtract(totalDescontos).subtract(acrescimoEmLote);
		
		if (compra.getObservacaoVerificacao() != null && !compra.getObservacaoVerificacao().equals("")){
			String[] observacoes = compra.getObservacaoVerificacao().split(";");
			
			for (String string : observacoes) {
				if (string.contains("adiantamento"))
					observacaoAdiantamentoZero = string;
				else if(string.contains("peixe"))
					observacaoAcrescimoPeso = string;
				else
					observacaoAcrescimoValor = string;
			}
		}

		carregarPagamentos();

	}

	public void carregarPagamentos() {

		pagDinheiro = BigDecimal.ZERO;
		pagAdiantamentoDinheiro = BigDecimal.ZERO;
		pagAdiantamentoCheque = BigDecimal.ZERO;
		pagAdiantamentoTransferencia = BigDecimal.ZERO;
		pagTransferencia = BigDecimal.ZERO;
		pagCheque = BigDecimal.ZERO;

		pagamentos = new ArrayList<Pagamento>();

		List<Pagamento> list = pagDao.getPagamentoPorCompra(compra);

		for (Pagamento pagamento : list) {
			if (pagamento.getTipoPagamento().equals(TipoPagamento.DINHEIRO)) {
				pagamento.setTipoDoPagamento("DINHEIRO");
				pagDinheiro = pagDinheiro.add(pagamento.getValor());
			}else if (pagamento.getTipoPagamento().equals(TipoPagamento.ADIANTAMENTO)) {
				pagamento.setTipoDoPagamento("ADIANTAMENTO");
				if (pagamento.getAdiantamento() == null) {
					pagamento.setAdiantamento(new AdiantamentoFornecedor());
					pagamento.getAdiantamento().setTipo(TipoAdiantamento.DINHEIRO);
					pagAdiantamentoDinheiro = pagAdiantamentoDinheiro.add(pagamento.getValor());
				}else{
					if (pagamento.getAdiantamento().getTipo().equals(TipoAdiantamento.TRANSFERENCIA))
						pagAdiantamentoTransferencia = pagAdiantamentoTransferencia.add(pagamento.getValor());
					else if (pagamento.getAdiantamento().getTipo().equals(TipoAdiantamento.CHEQUE))
						pagAdiantamentoCheque = pagAdiantamentoCheque.add(pagamento.getValor());
				}
			}else if (pagamento.getTipoPagamento().equals(TipoPagamento.CHEQUE)) {
				pagamento.setTipoDoPagamento("CHEQUE");
				pagCheque = pagCheque.add(pagamento.getValor());
			}else {
				pagamento.setTipoDoPagamento("TRANSFERENCIA");
				pagTransferencia = pagTransferencia.add(pagamento.getValor());
			}

			pagamentos.add(pagamento);
		}

		list = null;
	}
	
	public String cancelar(){
		return "compras_finalizadas?faces-redirect=true;";
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
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

	public Observacao getObservacao() {
		return observacao;
	}

	public void setObservacao(Observacao observacao) {
		this.observacao = observacao;
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

	public Integer getTotalQtdeCacapa() {
		return totalQtdeCacapa;
	}

	public void setTotalQtdeCacapa(Integer totalQtdeCacapa) {
		this.totalQtdeCacapa = totalQtdeCacapa;
	}

	public BigDecimal getTotalCompra() {
		return totalCompra;
	}

	public void setTotalCompra(BigDecimal totalCompra) {
		this.totalCompra = totalCompra;
	}

	public BigDecimal getTotalDescontos() {
		return totalDescontos;
	}

	public void setTotalDescontos(BigDecimal totalDescontos) {
		this.totalDescontos = totalDescontos;
	}

	public BigDecimal getTotalAcrescimos() {
		return totalAcrescimos;
	}

	public void setTotalAcrescimos(BigDecimal totalAcrescimos) {
		this.totalAcrescimos = totalAcrescimos;
	}

	public BigDecimal getPagDinheiro() {
		return pagDinheiro;
	}

	public void setPagDinheiro(BigDecimal pagDinheiro) {
		this.pagDinheiro = pagDinheiro;
	}

	public BigDecimal getPagTransferencia() {
		return pagTransferencia;
	}

	public void setPagTransferencia(BigDecimal pagTransferencia) {
		this.pagTransferencia = pagTransferencia;
	}

	public BigDecimal getPagCheque() {
		return pagCheque;
	}

	public void setPagCheque(BigDecimal pagCheque) {
		this.pagCheque = pagCheque;
	}

	public BigDecimal getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}

	public BigDecimal getFaltaPagar() {
		return faltaPagar;
	}

	public void setFaltaPagar(BigDecimal faltaPagar) {
		this.faltaPagar = faltaPagar;
	}

	public BigDecimal getTotalAdiantamento() {
		return totalAdiantamento;
	}

	public void setTotalAdiantamento(BigDecimal totalAdiantamento) {
		this.totalAdiantamento = totalAdiantamento;
	}

	public String getObservacaoAdiantamentoZero() {
		return observacaoAdiantamentoZero;
	}

	public void setObservacaoAdiantamentoZero(String observacaoAdiantamentoZero) {
		this.observacaoAdiantamentoZero = observacaoAdiantamentoZero;
	}

	public String getObservacaoAcrescimoPeso() {
		return observacaoAcrescimoPeso;
	}

	public void setObservacaoAcrescimoPeso(String observacaoAcrescimoPeso) {
		this.observacaoAcrescimoPeso = observacaoAcrescimoPeso;
	}

	public String getObservacaoAcrescimoValor() {
		return observacaoAcrescimoValor;
	}

	public void setObservacaoAcrescimoValor(String observacaoAcrescimoValor) {
		this.observacaoAcrescimoValor = observacaoAcrescimoValor;
	}

	public BigDecimal getPagAdiantamentoDinheiro() {
		return pagAdiantamentoDinheiro;
	}

	public void setPagAdiantamentoDinheiro(BigDecimal pagAdiantamentoDinheiro) {
		this.pagAdiantamentoDinheiro = pagAdiantamentoDinheiro;
	}

	public BigDecimal getPagAdiantamentoCheque() {
		return pagAdiantamentoCheque;
	}

	public void setPagAdiantamentoCheque(BigDecimal pagAdiantamentoCheque) {
		this.pagAdiantamentoCheque = pagAdiantamentoCheque;
	}

	public BigDecimal getPagAdiantamentoTransferencia() {
		return pagAdiantamentoTransferencia;
	}

	public void setPagAdiantamentoTransferencia(BigDecimal pagAdiantamentoTransferencia) {
		this.pagAdiantamentoTransferencia = pagAdiantamentoTransferencia;
	}

	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}
}
