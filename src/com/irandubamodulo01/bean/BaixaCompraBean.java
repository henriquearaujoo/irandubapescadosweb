package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.irandubamodulo01.dao.*;
import com.irandubamodulo01.enumerated.TipoAdiantamento;
import com.irandubamodulo01.model.*;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import com.irandubamodulo01.enumerated.StatusCompra;
import com.irandubamodulo01.enumerated.TipoHistoricoCompra;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.FormatterUtil;
import com.irandubamodulo01.util.ReportUtil;

@Named(value = "baixaCompraBean")
@ViewScoped
public class BaixaCompraBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private @Inject Compra compra;
	private @Inject Lote lote;
	private @Inject CompraDAO compraDAO; // = new CompraDAOImpl();
	private @Inject HistoricoAlteracaoValorDao historicoDAO;
	private @Inject LoteDAO loteDAO;
	private @Inject AdiantamentoDAO adiantDao;
	private @Inject PagamentoDAO pagDao;
	private @Inject ObservacaoDAO obsDao;
	private @Inject ConfiguracoesDAO configDao;
	private @Inject ContaDAO contaDao;
	private @Inject HistoricoUsuarioCompraDAO hisUsuCompraDao;
	private @Inject FornecedorDAO fornecedorDAO;
	private @Inject BarcoDAO barcoDAO;
	private @Inject ArquivoDAO arquivoDAO;

	// Filtros para busca das compras
	private Filtro filtro = new Filtro();
	// Lista da tabela
	private LazyDataModel<Compra> model;

	// Totais da compra
	private BigDecimal totalPesoBruto = BigDecimal.ZERO;
	private BigDecimal totalPesoLiquido = BigDecimal.ZERO;
	private Integer totalQtdeCacapa = 0;
	private BigDecimal totalCompra = BigDecimal.ZERO;

	// totais descontos e acrescimo da compra
	private BigDecimal totalDescontos = BigDecimal.ZERO;
	private BigDecimal totalAcrescimos = BigDecimal.ZERO;

	private Integer filtroStatus;
	private Integer tipoAlteracaoValor;
	private Integer tipoValor;

	// Variavel usada para pegar o valor ja alterado após uma alteração ou
	// desconto
	private BigDecimal valorAlterado;

	private BigDecimal novoValorKGPeixe;

	// Usado para testar se houve alteracao diretamente no total da compra e não
	// apenas pelo item.
	private Boolean alteracaoTotalCompra;

	// Tipos de pagamentos e totais que podem ser futuramente refatorar esses
	// pagamentos para uma classe Pagamentos com os atributos a baixo para
	// ... manipulação dos valores
	private BigDecimal pagDinheiro;
	private BigDecimal pagTransferencia;
	private BigDecimal pagCheque;
	private BigDecimal pagAdiantamentoDinheiro;
	private BigDecimal pagAdiantamentoCheque;
	private BigDecimal pagAdiantamentoTransferencia;
	private BigDecimal totalPago;
	private BigDecimal faltaPagar;
	private BigDecimal totalAdiantamento;

	// Tipo de pagamento criado por que em um futuro pode ser que cada pagamento
	// tenha um comportamento diferente ex: uma transferencia
	// ... pode ser feita de imediato ou um adiantamento pode ter descontos
	// incluídos
	private TipoPagamento tipoPagamento;

	// Mensagens utilitarias que posteriormente pode ser refatorada para uma
	// classe utilitaria de mensagens ou um arquivo properties
	private String mensagemConfirmacaoPagamento;
	private String msgCampoTransferencia;
	private String msgCampoCheque;
	private String msgCampoTransferenciaAdiantamento;
	private String msgCampoChequeAdiantamento;

	// Lista de pagamentos e adiantamentos usada para calcular o valor total
	private List<AdiantamentoFornecedor> adiantamentos;
	private List<SelectItem> listaDecontas;

	// Variaveis utilitarias para definir habilitaçoes de botoes e labels
	private Boolean disableButtonPagar;
	private Boolean mostrarMensagem;

	// String utiliraria para definir uma cor no dialog de confirmação de
	// pagamento, futuramente refatorar utilidade
	// ... para uma classe utilitaria
	private String corConfirmacaoPagamento;

	private Boolean autorizado;
	private List<SelectItem> listaStatus;
	private Usuario usuario;

	// Variaveis para checagem dos campos (Transferencia e Chque)
	private Boolean preencheCampoTransferencia;
	private Boolean preencheCampoCheque;
	private Boolean preencheCampoTranferenciaAdiantamento;
	private Boolean preencheCampoChequeAdiantamento;

	// Informações adicionais de pagamento
	private Conta conta;
	private String nomeCheque;
	private String numeroCheque;
	private Date dataCheque;

	// Observações geradas pelo sistema
	private String observacaoAdiantamentoZero;
	private String observacaoAcrescimoPeso;
	private String observacaoAcrescimoValor;
	// Observação gerada pelo o usuario
	private Observacao observacao;
	private String observacaoHistorico;
	// Valor minimo exigido no adiantamento se houver adiantamento pendente
	private BigDecimal valorMinimoAdiantamento;

	private String nomeFornecedor;

	private String nomeBarco;

	private List<Fornecedor> fornecedores;

	private List<Barco> barcos;

	private List<SelectItem> chequesAdiantamento;

	private List<SelectItem> contasAdiantamento;

	//Utilizados na nova forma de pagamento dinamico
	private Pagamento pagamento;
	private List<Pagamento> pagamentos;
	private AdiantamentoFornecedor adiantamentoPagamento;
	private TipoAdiantamento tipoAdiantamentoPagamento;
	private String tipoDoPagamento;

	private StatusCompra status;

	public BaixaCompraBean() {
		usuario = getUsuarioSession().getUsuario();
		carregarFiltroStatus();
	}

	public UsuarioSessionBean getUsuarioSession(){
		UsuarioSessionBean usuarioSession = (UsuarioSessionBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuariologado");
		return usuarioSession;
	}

	public StatusCompra getStatusDaCompra(){
		StatusCompra status = (StatusCompra) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("statusCompra");
		return status;
	}

	public void limparStatusDaCompra(){
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("statusCompra", null);
	}

	public void carregarFiltroStatus() {
		listaStatus = new ArrayList<SelectItem>();

		listaStatus.add(new SelectItem(0, "Enviados"));

		if (usuario.getPerfil().getDescricao().equals("Admin")) {
			listaStatus.add(new SelectItem(-1, "Todas"));
			listaStatus.add(new SelectItem(1, "Pagas"));
			filtroStatus = -1;
		} else
			filtroStatus = 0;
	}

	public void carregarCompras() {

		if (getStatusDaCompra() != null)
			filtro.setStatusCompra(getStatusDaCompra());
		else
			filtro.setStatusCompra(status);

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

				usuario = getUsuarioSession().getUsuario();

				if (!usuario.getPerfil().getDescricao().equals("Admin")) {
					filtro.setAutorizado(null);
					filtro.setPause(false);
				}

				usuario = null;
				setRowCount(compraDAO.quantidadeFiltrados(filtro));
				return compraDAO.filtrados(filtro);
			}

		};

		limparStatusDaCompra();
	}


	public void carregarDetalhesCompra() {

		observacao = new Observacao();
		inicializarPagamento();

		compra.setHistoricos(historicoDAO.getHistoricoPorCompra(compra.getId().longValue()));

		zerarTotais();

		carregarPagamentos();

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
		for (HistoricoAlteracaoValor historico : compra.getHistoricos()) {
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

		totalCompra = totalCompra.add(totalAcrescimos).add(descontoEmLote).subtract(totalDescontos).subtract(acrescimoEmLote);

		if (compra.getObservacaoVerificacao() != null
				&& !compra.getObservacaoVerificacao().equals("")) {
			String[] observacoes = compra.getObservacaoVerificacao().split(";");

			for (String string : observacoes) {
				if (string.contains("adiantamento"))
					observacaoAdiantamentoZero = string;
				else if (string.contains("peixe"))
					observacaoAcrescimoPeso = string;
				else
					observacaoAcrescimoValor = string;
			}
		}

		if (compra.getFornecedor().getNome().toLowerCase().equals("outro")){
			setFornecedores(fornecedorDAO.getFornecedoresComplete());
			setNomeFornecedor(compra.getFornecedor().getNome());
		}

		if (compra.getBarco().getNome().toLowerCase().equals("outro")){
			setBarcos(new ArrayList<Barco>());
			setNomeBarco(compra.getBarco().getNome());
		}

	}

	public List<String> completeFornecedores(String query){
		List<String> results = new ArrayList<String>();
		for(Fornecedor fornecedor : getFornecedores()){
			if (fornecedor.getNome().toLowerCase().startsWith(query.toLowerCase()))
				results.add(fornecedor.getNome());
		}
		return results;
	}

	public List<String> completeBarcos(String query){
		List<String> results = new ArrayList<String>();
		for(Barco barco : getBarcos()){
			if (barco.getNome().toLowerCase().startsWith(query.toLowerCase()))
				results.add(barco.getNome());
		}
		return results;
	}

	public void selecionaFornecedor(SelectEvent event){

		compra.setFornecedor(fornecedorDAO.getFornecedorPorNome(event.getObject().toString()));

		if (compra.getBarco().getNome().toLowerCase().equals("outro")){
			setBarcos(barcoDAO.getBarcosComplete(compra.getFornecedor()));
			if (getBarcos() != null && getBarcos().size() > 0)
				setNomeBarco(getBarcos().get(0).getNome());
		}
	}

	public void atualizarBarcoEFornecedor(){

		try{
			if (compra.getBarco().getNome().toLowerCase().equals("outro"))
				compra.setBarco(barcoDAO.getBarcoPorNomeEFornecedor(nomeBarco, compra.getFornecedor()));

			compraDAO.save(compra);

			addMessage("Informação", "Compra atualizada com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possivel atualizar a compra.", FacesMessage.SEVERITY_ERROR);
		}

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

	public void carregarContasFornecedor() {
		listaDecontas = new ArrayList<SelectItem>();

		List<Conta> contas = contaDao.getContasPorFornecedor(compra
				.getFornecedor());
		for (Conta conta : contas) {
			listaDecontas.add(new SelectItem(conta, conta.getAgencia() + "/"
					+ conta.getConta()));
		}
	}

	public void prepararValores() {
		buscarAdiantamentosEpagamentos();
		calcularAdiantamento();
		carregarContasFornecedor();
		carregarPagamentos();

		valorMinimoAdiantamento = configDao.getConfiguracao().getValorMinimoDebitoAdiantamento();

		//totalPago = BigDecimal.ZERO;

		faltaPagar = compra.getValorTotal().subtract(getTotalPago());
		conta = new Conta();
		conta.setFornecedor(new Fornecedor());
	}

	public void gerarResumoDeCompra() {
		observacao = new Observacao();

		observacaoAdiantamentoZero = "";
		observacaoAcrescimoValor = "";
		observacaoAcrescimoPeso = "";
		autorizado = compra.getAutorizado() != null ? compra.getAutorizado()
				: false;
		//totalPago = BigDecimal.ZERO;
		//totalPago = totalPago.add(pagDinheiro).add(pagAdiantamentoDinheiro).add(pagAdiantamentoCheque).add(pagAdiantamentoTransferencia).add(pagTransferencia).add(pagCheque);
		if (getTotalPago().intValue() > compra.getValorTotal().intValue()) {
			mensagemConfirmacaoPagamento = "Total pago é maior que o valor total da compra.";
			corConfirmacaoPagamento = "#FF0000";
			disableButtonPagar = true;
		} else if (getTotalPago().intValue() < compra.getValorTotal().intValue()) {
			corConfirmacaoPagamento = "#FF0000";
			disableButtonPagar = true;
			mensagemConfirmacaoPagamento = "Total pago é menor que o valor total da compra.";
		} else {

			BigDecimal totalPagAdiantamento = pagamentos.stream().filter(p -> p.getTipoDoPagamento().equals("ADIANTAMENTO")).map(Pagamento::getValor).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

			if (totalPagAdiantamento == null || (totalPagAdiantamento.compareTo(valorMinimoAdiantamento) == -1)) {
				if (totalAdiantamento.compareTo(BigDecimal.ZERO) > 0) {
					autorizado = false;
					disableButtonPagar = false;
					corConfirmacaoPagamento = "YELLOW";
					mensagemConfirmacaoPagamento = "Enviar compra para autorização.";
					observacaoAdiantamentoZero = "Fornecedor com adiantamento pendente  no valor de "
							+ totalAdiantamento + " não descontado na compra.";

				} else {
					observacaoAdiantamentoZero = null;
					autorizado = true;
					disableButtonPagar = false;
					corConfirmacaoPagamento = "GREEN";
					mensagemConfirmacaoPagamento = "Pagamento confirmado.";
				}

			} else {
				autorizado = true;
				disableButtonPagar = false;
				corConfirmacaoPagamento = "GREEN";
				mensagemConfirmacaoPagamento = "Pagamento confirmado.";
			}

			if (buscarHistoricoDeAcrescimo() != null) {

				autorizado = false;
				disableButtonPagar = false;
				corConfirmacaoPagamento = "YELLOW";
				mensagemConfirmacaoPagamento = "Enviar compra para autorização.";
			}

		}
		pagDinheiro = pagamentos.stream().filter(p -> p.getTipoDoPagamento().equals("DINHEIRO")).map(Pagamento::getValor).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
		pagTransferencia = pagamentos.stream().filter(p -> p.getTipoDoPagamento().equals("TRANSFERENCIA")).map(Pagamento::getValor).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
		pagCheque = pagamentos.stream().filter(p -> p.getTipoDoPagamento().equals("CHEQUE")).map(Pagamento::getValor).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
		pagAdiantamentoDinheiro = pagamentos.stream().filter(p -> p.getTipoDoPagamento().equals("ADIANTAMENTO") && p.getAdiantamento() != null && p.getAdiantamento().getTipo().equals(TipoAdiantamento.DINHEIRO)).map(Pagamento::getValor).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
		pagAdiantamentoCheque = pagamentos.stream().filter(p -> p.getTipoDoPagamento().equals("ADIANTAMENTO") && p.getAdiantamento() != null && p.getAdiantamento().getTipo().equals(TipoAdiantamento.CHEQUE)).map(Pagamento::getValor).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
		pagAdiantamentoTransferencia = pagamentos.stream().filter(p -> p.getTipoDoPagamento().equals("ADIANTAMENTO") && p.getAdiantamento() != null && p.getAdiantamento().getTipo().equals(TipoAdiantamento.TRANSFERENCIA)).map(Pagamento::getValor).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

		/*mostrarMensagem = false;
		preencheCampoCheque = false;
		preencheCampoTransferencia = false;
		preencheCampoChequeAdiantamento = false;
		preencheCampoTranferenciaAdiantamento = false;
		if ((pagTransferencia.compareTo(BigDecimal.ZERO) > 0)
				|| (pagCheque.compareTo(BigDecimal.ZERO) > 0)
				|| (pagAdiantamentoTransferencia.compareTo(BigDecimal.ZERO) > 0)
				|| (pagAdiantamentoCheque.compareTo(BigDecimal.ZERO) > 0)) {
			verificarCamposAdicionais();
		}*/

		faltaPagar = BigDecimal.ZERO;
		faltaPagar = faltaPagar.add(compra.getValorTotal().subtract(getTotalPago()));
	}

	public void inicializarPagamento(){
		pagamento = new Pagamento();
		pagamento.setNovo(true);
		pagamento.setValor(BigDecimal.ZERO);
		pagamento.setAdiantamento(new AdiantamentoFornecedor());
		pagamento.getAdiantamento().setTipo(TipoAdiantamento.DINHEIRO);
		if (pagamentos == null)
			pagamentos = new ArrayList<Pagamento>();

		pagamento.setId(new Long(pagamentos.size() + 1));

		tipoDoPagamento = "DINHEIRO";
		tipoAdiantamentoPagamento = null;
		adiantamentoPagamento = null;
	}

	public void adicionarPagamento(){

		if (pagamento.getValor().compareTo(BigDecimal.ZERO) < 1){
			addMessage("Aviso", "Digite um valor para o pagamento.", FacesMessage.SEVERITY_WARN);
			return;
		}

		pagamento.setTipoDoPagamento(tipoDoPagamento);

		if (tipoDoPagamento.equals("DINHEIRO")){
			pagamento.setTipoPagamento(TipoPagamento.DINHEIRO);
			pagamentos.add(pagamento);
		}else if (tipoDoPagamento.equals("CHEQUE")){
			if (pagamento.getNumeroCheque() != null && !pagamento.getNumeroCheque().isEmpty() && pagamento.getNomeCheque() != null && !pagamento.getNomeCheque().isEmpty()){
				pagamento.setTipoPagamento(TipoPagamento.CHEQUE);
				pagamentos.add(pagamento);
			}else{
				addMessage("Aviso", "Preencha o nome e o número do cheque.", FacesMessage.SEVERITY_WARN);
				return;
			}
		}else if (tipoDoPagamento.equals("TRANSFERENCIA")){
			if (pagamento.getConta() != null){
				pagamento.setTipoPagamento(TipoPagamento.TRANSFERENCIA);
				pagamentos.add(pagamento);
			}else{
				addMessage("Aviso", "Selecione uma conta.", FacesMessage.SEVERITY_WARN);
				return;
			}
		}else{

			if (adiantamentoPagamento == null){
				pagamento.setAdiantamento(new AdiantamentoFornecedor());
				pagamento.getAdiantamento().setTipo(tipoAdiantamentoPagamento);
			}else{
				pagamento.setAdiantamento(adiantamentoPagamento);
				pagamento.getAdiantamento().setTipo(tipoAdiantamentoPagamento);
			}

			if (pagamento.getAdiantamento().getTipo() != null) {
				if (pagamento.getAdiantamento().getTipo().equals(TipoAdiantamento.DINHEIRO)) {
					pagamento.setTipoPagamento(TipoPagamento.ADIANTAMENTO);
					pagamentos.add(pagamento);
				} else if (pagamento.getAdiantamento().getTipo().equals(TipoAdiantamento.CHEQUE)) {
					if (pagamento.getAdiantamento() != null && pagamento.getAdiantamento().getNumeroCheque() != null && !pagamento.getAdiantamento().getNumeroCheque().isEmpty() && pagamento.getAdiantamento().getNomeCheque() != null && !pagamento.getAdiantamento().getNomeCheque().isEmpty()) {
						pagamento.setTipoPagamento(TipoPagamento.ADIANTAMENTO);
						pagamentos.add(pagamento);
					} else {
						addMessage("Aviso", "Selecione um cheque para o adiantamento.", FacesMessage.SEVERITY_WARN);
						return;
					}
				} else {
					if (pagamento.getAdiantamento() != null) {
						pagamento.setTipoPagamento(TipoPagamento.ADIANTAMENTO);
						pagamentos.add(pagamento);
					} else {
						addMessage("Aviso", "Selecione uma conta para o adiantamento.", FacesMessage.SEVERITY_WARN);
						return;
					}
				}
			}else{
				addMessage("Aviso", "Selecione o tipo do adiantamento.", FacesMessage.SEVERITY_WARN);
				return;
			}
		}

		inicializarPagamento();
	}

	public void excluirPagamento(){

		if (!pagamento.getNovo()){
			List<Arquivo> arquivos = arquivoDAO.getArquivosPorPagamento(pagamento);

			if (arquivos != null && arquivos.size() > 0) {
				addMessage("Aviso", "Não é possível excluir o pagamento.", FacesMessage.SEVERITY_ERROR);
				return;
			}

			pagDao.remove(pagamento);

			carregarPagamentos();
		}else {
			List<Pagamento> temp = new ArrayList<Pagamento>();

			temp.addAll(pagamentos);

			for (Pagamento p : temp) {
				if (p.getId().longValue() == pagamento.getId().longValue())
					pagamentos.remove(p);
			}

			temp = null;
		}

		inicializarPagamento();
	}

	public String pagarCompra() {

		Boolean pagamentoPendente = false;
		HistoricoUsuarioCompra histUsuCompra = new HistoricoUsuarioCompra();
		compra.setAutorizado(autorizado);
		//pagDao.removePagamentosPorCompra(compra);

		for(Pagamento pag : pagamentos){
			if (pag.getTipoDoPagamento().equals("DINHEIRO")){
				if (pag.getNovo())
					TipoPagamento.DINHEIRO.getExecutaPagamento().executaPagamento(pag.getValor(), compra, pagDao, null, null, null, null);
				else
					TipoPagamento.DINHEIRO.getExecutaPagamento().alterarPagamento(pag, pagDao);
			}else if (pag.getTipoDoPagamento().equals("CHEQUE")){
				if (pag.getNovo())
					TipoPagamento.CHEQUE.getExecutaPagamento().executaPagamento(pag.getValor(), compra, pagDao, null, pag.getNomeCheque(), pag.getNumeroCheque(), null);
				else
					TipoPagamento.CHEQUE.getExecutaPagamento().alterarPagamento(pag, pagDao);
			}else if (pag.getTipoDoPagamento().equals("TRANSFERENCIA")){
				if (pag.getNovo())
					TipoPagamento.TRANSFERENCIA.getExecutaPagamento().executaPagamento(pag.getValor(), compra, pagDao, pag.getConta(), null, null, null);
				else
					TipoPagamento.TRANSFERENCIA.getExecutaPagamento().alterarPagamento(pag, pagDao);

				pagamentoPendente = true;
			}else{
				if (pag.getAdiantamento().getTipo().equals(TipoAdiantamento.DINHEIRO)){
					if (pag.getNovo())
						TipoPagamento.ADIANTAMENTO.getExecutaPagamento().executaPagamento(pag.getValor(), compra, pagDao, null, null, null, null);
					else {
						pag.setAdiantamento(null);
						TipoPagamento.ADIANTAMENTO.getExecutaPagamento().alterarPagamento(pag, pagDao);
					}
				}else if (pag.getAdiantamento().getTipo().equals(TipoAdiantamento.CHEQUE)){
					if (pag.getNovo())
						TipoPagamento.ADIANTAMENTO.getExecutaPagamento().executaPagamento(pag.getValor(), compra, pagDao, null, null, null, pag.getAdiantamento());
					else
						TipoPagamento.ADIANTAMENTO.getExecutaPagamento().alterarPagamento(pag, pagDao);
				}else{
					if (pag.getNovo())
						TipoPagamento.ADIANTAMENTO.getExecutaPagamento().executaPagamento(pag.getValor(), compra, pagDao, null, null, null, pag.getAdiantamento());
					else
						TipoPagamento.ADIANTAMENTO.getExecutaPagamento().alterarPagamento(pag, pagDao);
				}
			}
		}

		/*TipoPagamento.ADIANTAMENTO.getExecutaPagamento().executaPagamento(pagAdiantamentoDinheiro, compra, pagDao, null, null, null, null);
		TipoPagamento.ADIANTAMENTO.getExecutaPagamento().executaPagamento(pagAdiantamentoCheque, compra, pagDao, null, null, null, adiantamentoCheque);
		TipoPagamento.ADIANTAMENTO.getExecutaPagamento().executaPagamento(pagAdiantamentoTransferencia, compra, pagDao, null, null, null, adiantamentoTransferencia);

		TipoPagamento.DINHEIRO.getExecutaPagamento().executaPagamento(pagDinheiro, compra, pagDao, null, null, null, null);
		TipoPagamento.TRANSFERENCIA.getExecutaPagamento().executaPagamento(pagTransferencia, compra, pagDao, conta, null, null, null);
		TipoPagamento.CHEQUE.getExecutaPagamento().executaPagamento(pagCheque, compra, pagDao, null, nomeCheque, numeroCheque, null);*/

		if (compra.getAutorizado()) {
			if (!pagamentoPendente)
				compra.setStatusCompra(StatusCompra.PAGO);
			else
				compra.setStatusCompra(StatusCompra.FINALIZADA_PAGAMENTO_PENDENTE);
			compra.setStatus(true);
			histUsuCompra.setTipo(TipoHistoricoCompra.FINALIZOU);
		} else {
			compra.setStatusCompra(StatusCompra.AGUARDA_AUTORIZACAO);
			compra.setStatus(false);
			histUsuCompra.setTipo(TipoHistoricoCompra.ENVIOU_AUTORIZACAO);
		}

		compra.setObservacaoVerificacao("");

		if (observacaoAdiantamentoZero != null
				&& !observacaoAdiantamentoZero.equals(""))
			compra.setObservacaoVerificacao(compra.getObservacaoVerificacao()
					+ observacaoAdiantamentoZero);

		if (observacaoAcrescimoValor != null
				&& !observacaoAcrescimoValor.equals(""))
			compra.setObservacaoVerificacao(compra.getObservacaoVerificacao()
					.equals("") ? compra.getObservacaoVerificacao()
					+ observacaoAcrescimoValor : compra
					.getObservacaoVerificacao()
					+ ";"
					+ observacaoAcrescimoValor);

		if (observacaoAcrescimoPeso != null
				&& !observacaoAcrescimoPeso.equals(""))
			compra.setObservacaoVerificacao(compra.getObservacaoVerificacao()
					.equals("") ? compra.getObservacaoVerificacao()
					+ observacaoAcrescimoPeso : compra
					.getObservacaoVerificacao() + ";" + observacaoAcrescimoPeso);
		try {
			
			if (!observacao.getObservacao().equalsIgnoreCase("")) {
				observacao.setCompra(compra);
				observacao.setData(new Date());
				obsDao.save(observacao);
			}

			compraDAO.save(compra);

			observacao = new Observacao();
			observacao.setUsuario(getUsuarioSession().getUsuario());
			observacao.setCompra(compra);
			observacao.setData(new Date());
			if (histUsuCompra.getTipo().equals(TipoHistoricoCompra.ENVIOU_AUTORIZACAO)) {
				observacao.setObservacao("Compra enviada para autorização.");
				obsDao.save(observacao);
			}else if (histUsuCompra.getTipo().equals(TipoHistoricoCompra.FINALIZOU)) {
				observacao.setObservacao("Compra foi finalizada.");
				obsDao.save(observacao);
			}

			carregarCompras();
			zerarTotais();
			addMessage("Informação: ", "Compra finalizada com sucesso.",
					FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (compra.getStatusCompra().equals(StatusCompra.PAGO))
			return null;

		return "baixa_compra?faces-redirect=true;";

	}

	public String redirecionarBaixaCompra() {
		return "baixa_compra?faces-redirect=true;";
	}

	public String prepararDetalhesCompra() {
		return "detalhescompra?faces-redirect=true&amp;includeViewParams=true";
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
			pagamento.setNovo(false);
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
				numeroCheque = pagamento.getNumeroCheque();
				nomeCheque = pagamento.getNomeCheque();
			}else {
				pagamento.setTipoDoPagamento("TRANSFERENCIA");
				pagTransferencia = pagTransferencia.add(pagamento.getValor());
			}

			pagamentos.add(pagamento);
		}

		list = null;
	}

	public String autorizarCompra() {
		if (!observacao.getObservacao().equalsIgnoreCase("")) {
			observacao.setCompra(compra);
			observacao.setData(new Date());
			obsDao.save(observacao);
		}
		compra.setStatus(false);
		compra.setStatusCompra(StatusCompra.AUTORIZADO);
		compra.setAutorizado(true);
		compraDAO.save(compra);
		gerarHistoricoUsuarioCompra(TipoHistoricoCompra.AUTORIZOU);
		carregarCompras();
		zerarTotais();

		addMessage("Informação: ", "Compra autorizada com sucesso.",
				FacesMessage.SEVERITY_INFO);

		return "baixa_compra?faces-redirect=true;";
	}

	public String retornarCompra() {

		if (!observacao.getObservacao().equalsIgnoreCase("")) {
			observacao.setCompra(compra);
			observacao.setData(new Date());
			obsDao.save(observacao);
		}

		compra.setStatus(false);
		compra.setStatusCompra(StatusCompra.RETORNADO);
		compra.setAutorizado(null);
		compraDAO.save(compra);	
		gerarHistoricoUsuarioCompra(TipoHistoricoCompra.RETORNOU);
		carregarCompras();
		zerarTotais();

		addMessage("Informação: ", "Compra retornada.",
				FacesMessage.SEVERITY_INFO);

		return "baixa_compra?faces-redirect=true;";

	}

	public String retornarCompraInicio() {

		if (!observacao.getObservacao().equalsIgnoreCase("")) {
			observacao.setCompra(compra);
			observacao.setData(new Date());
			obsDao.save(observacao);
		}

		compra.setStatus(false);
		compra.setStatusCompra(StatusCompra.RETORNADO_INICIO);
		compra.setAutorizado(null);
		compra.setPause(true);
		compraDAO.save(compra);
		gerarHistoricoUsuarioCompra(TipoHistoricoCompra.RETORNOU_INICIO);
		carregarCompras();
		zerarTotais();

		addMessage("Informação: ", "Compra retornada.",
				FacesMessage.SEVERITY_INFO);

		return "baixa_compra?faces-redirect=true;";

	}
	
	public void gerarHistoricoUsuarioCompra(TipoHistoricoCompra tipo){

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

		obsDao.save(obs);
	}

	public String concluirCompra() {
		
		if (!observacao.getObservacao().equalsIgnoreCase("")) {
			observacao.setCompra(compra);
			observacao.setData(new Date());
			obsDao.save(observacao);
		}
		compra.setStatus(true);
		compra.setAutorizado(true);
		compra.setStatusCompra(StatusCompra.PAGO);
		compraDAO.save(compra);
		gerarHistoricoUsuarioCompra(TipoHistoricoCompra.FINALIZOU);
		carregarCompras();
		zerarTotais();

		addMessage("Informação: ", "Compra finalizada com sucesso.",
				FacesMessage.SEVERITY_INFO);
		return "baixa_compra?faces-redirect=true;";
	}

	public String cancelar() {
		return "baixa_compra?faces-redirect=true;";
	}

	public void verificarCamposAdicionais() {

		if ((pagTransferencia.compareTo(BigDecimal.ZERO) > 0)) {
			if (conta.getBeneficiado().equals("")
					|| conta.getAgencia().equals("")
					|| conta.getConta().equals("")) {

				preencheCampoTransferencia = true;
				msgCampoTransferencia = "Por favor, preencha todas as informações de transferência.";

			} else {
				preencheCampoTransferencia = false;
			}
		}

		if ((pagCheque.compareTo(BigDecimal.ZERO) > 0)) {
			if (nomeCheque == null || numeroCheque == null || nomeCheque.isEmpty() || numeroCheque.isEmpty()) {
				preencheCampoCheque = true;
				msgCampoCheque = "Por favor, preencha todas as informações do cheque.";

			}else {
				preencheCampoCheque = false;
			}
		}

		if (preencheCampoCheque || preencheCampoTransferencia || preencheCampoChequeAdiantamento || preencheCampoTranferenciaAdiantamento) {
			mostrarMensagem = true;
		}

	}

	public void prepararAlteracaoValor() {
		observacao = new Observacao();
		alteracaoTotalCompra = false;
		tipoAlteracaoValor = 3;
		tipoValor = null;
	}

	public void prepararAlteracaoTotalDoValor() {
		alteracaoTotalCompra = true;
		tipoAlteracaoValor = null;
		tipoValor = null;

	}

	public void alterarValorItem() {

		if (novoValorKGPeixe != null && novoValorKGPeixe != BigDecimal.ZERO) {
			valorAlterado = lote.getPesoLiquido().multiply(novoValorKGPeixe);
			lote.setValorUnitarioPeixe(novoValorKGPeixe);
			lote.setValor(valorAlterado);

		}

		if (tipoAlteracaoValor != null) {
			if (tipoAlteracaoValor.intValue() == 1) {
				if (tipoValor.intValue() == 1) {

					if (!alteracaoTotalCompra) {
						valorAlterado = lote.getValor().subtract(valorAlterado);
						lote.setValor(valorAlterado);
					} else {
						valorAlterado = compra.getValorTotal().subtract(
								valorAlterado);
						criarHistoricoAlteracaoNoTotal();
					}
				} else {

					if (!alteracaoTotalCompra) {
						valorAlterado = valorAlterado.divide(new BigDecimal(100)).multiply(lote.getValor());
						valorAlterado = lote.getValor().subtract(valorAlterado);
						lote.setValor(valorAlterado);
					} else {
						valorAlterado = valorAlterado.divide(
								new BigDecimal(100)).multiply(
								compra.getValorTotal());
						valorAlterado = compra.getValorTotal().subtract(
								valorAlterado);
						criarHistoricoAlteracaoNoTotal();
					}

				}
			} else if (tipoAlteracaoValor.intValue() == 2) {
				if (tipoValor.intValue() == 1) {

					if (!alteracaoTotalCompra) {
						valorAlterado = lote.getValor().add(valorAlterado);
						lote.setValor(valorAlterado);
					} else {
						valorAlterado = compra.getValorTotal().add(valorAlterado);
						criarHistoricoAlteracaoNoTotal();
					}

				} else {

					if (!alteracaoTotalCompra) {
						valorAlterado = valorAlterado.divide(new BigDecimal(100)).multiply(lote.getValor());
						valorAlterado = lote.getValor().add(valorAlterado);
						lote.setValor(valorAlterado);
					} else {
						valorAlterado = valorAlterado.divide(
								new BigDecimal(100)).multiply(
								compra.getValorTotal());
						valorAlterado = compra.getValorTotal().add(
								valorAlterado);
						criarHistoricoAlteracaoNoTotal();
					}

				}
			}

			try {
				if (!alteracaoTotalCompra){
					if (lote.getCompra() == null){
						lote.setCompra(new Compra());
						lote.setCompra(compra);
					}
					if (lote.getFornecedor() == null){
						lote.setFornecedor(new Fornecedor());
					    lote.setFornecedor(compra.getFornecedor());
					}
					
					loteDAO.save(lote);
				}

				carregarDetalhesCompra();
				compra.setValorTotal(totalCompra);
				compraDAO.save(compra);
				valorAlterado = null;
				novoValorKGPeixe = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void alterarValorItens() {

		this.lote.setTotalValorKG(BigDecimal.ZERO);
		this.lote.setTotalValor(BigDecimal.ZERO);
		this.lote.setTotalDesconto(BigDecimal.ZERO);
		this.lote.setTotalAcrescimo(BigDecimal.ZERO);

		for(Lote lote : compra.getLotes()) {

			if (this.lote.getPeixe().getDescricao().equals(lote.getPeixe().getDescricao())) {

				if (novoValorKGPeixe != null && novoValorKGPeixe != BigDecimal.ZERO) {
					BigDecimal valorAlteracao = lote.getPesoLiquido().multiply(novoValorKGPeixe);
					criarHistorico(lote, valorAlteracao);
					lote.setValorUnitarioPeixe(novoValorKGPeixe);
					lote.setValor(valorAlteracao);
				}

				if (tipoAlteracaoValor != null) {
					if (tipoAlteracaoValor.intValue() == 1) {
						if (tipoValor.intValue() == 1) {

							if (!alteracaoTotalCompra) {
								BigDecimal valorAlteracao = lote.getValor().subtract(valorAlterado);
								criarHistorico(lote, valorAlteracao);
								lote.setValor(valorAlteracao);
							} else {
								valorAlterado = compra.getValorTotal().subtract(valorAlterado);
								criarHistoricoAlteracaoNoTotal();
							}
						} else {

							if (!alteracaoTotalCompra) {
								BigDecimal valorAlteracao = valorAlterado.divide(new BigDecimal(100)).multiply(lote.getValor());
								valorAlteracao = lote.getValor().subtract(valorAlteracao);
								criarHistorico(lote, valorAlteracao);
								lote.setValor(valorAlteracao);
							} else {
								valorAlterado = valorAlterado.divide(new BigDecimal(100)).multiply(compra.getValorTotal());
								valorAlterado = compra.getValorTotal().subtract(valorAlterado);
								criarHistoricoAlteracaoNoTotal();
							}

						}
					} else if (tipoAlteracaoValor.intValue() == 2) {
						if (tipoValor.intValue() == 1) {

							if (!alteracaoTotalCompra) {
								BigDecimal valorAlteracao = lote.getValor().add(valorAlterado);
								criarHistorico(lote, valorAlteracao);
								lote.setValor(valorAlteracao);
							} else {
								valorAlterado = compra.getValorTotal().add(valorAlterado);
								criarHistoricoAlteracaoNoTotal();
							}

						} else {

							if (!alteracaoTotalCompra) {
								BigDecimal valorAlteracao = valorAlterado.divide(new BigDecimal(100)).multiply(lote.getValor());
								valorAlteracao = lote.getValor().add(valorAlteracao);
								criarHistorico(lote, valorAlteracao);
								lote.setValor(valorAlteracao);
							} else {
								valorAlterado = valorAlterado.divide(new BigDecimal(100)).multiply(compra.getValorTotal());
								valorAlterado = compra.getValorTotal().add(valorAlterado);
								criarHistoricoAlteracaoNoTotal();
							}

						}
					}

					try {
						if (!alteracaoTotalCompra) {
							if (lote.getCompra() == null) {
								lote.setCompra(new Compra());
								lote.setCompra(compra);
							}
							if (lote.getFornecedor() == null) {
								lote.setFornecedor(new Fornecedor());
								lote.setFornecedor(compra.getFornecedor());
							}

							loteDAO.save(lote);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				this.lote.setTotalValorKG(this.lote.getTotalValorKG().add(lote.getValorUnitarioPeixe()));
				this.lote.setTotalValor(this.lote.getTotalValor().add(lote.getValor()));
				this.lote.setTotalDesconto(this.lote.getTotalDesconto().add(lote.getDesconto()));
				this.lote.setTotalAcrescimo(this.lote.getTotalAcrescimo().add(lote.getAcrescimo()));
				this.lote.setValorUnitarioPeixe(lote.getValorUnitarioPeixe());
			}

		}

		carregarDetalhesCompra();
		compra.setValorTotal(totalCompra);
		compraDAO.save(compra);
		valorAlterado = null;
		novoValorKGPeixe = null;

	}

	@SuppressWarnings("unchecked")
	public void imprimirRecibo() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");

		String tiposPagamento = "";
		BigDecimal pagDinheiro = BigDecimal.ZERO;
		List<Pagamento> list = pagDao.getPagamentoPorCompra(compra);
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

		parametros
				.put("parametroValor",
						"R$ "
								+ FormatterUtil.getValorFormatado(compra
										.getValorTotal()));
		parametros.put("parametroQuantiaExtenso", FormatterUtil
				.valorPorExtenso(compra.getValorTotal().doubleValue()));
		parametros.put("parametroPagamento", "Compra de pescados");
		parametros.put("parametroNome", compra.getFornecedor().getNome());
		parametros.put("parametroCNPJ", "CNPJ: 04.408.929/0001-05");
		parametros.put("parametroEndereco", "Av. Beira Rio, s/n - Iranduba/AM");
		parametros.put("parametroCEP", "CEP: 69415-000");
		parametros.put("parametroEmpresa", "Iranduba Frigorífico de Pescados");
		parametros.put("parametroTiposPagamento", tiposPagamento);

		try {
			JasperDesign jd = JRXmlLoader.load(path + "relatorios/recibo.jrxml");
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

	public void limparFiltro() {
		filtro.setNome("");
		filtro.setDataInicio(null);
		filtro.setDataFinal(null);
		filtroStatus = 0;
	}

	public void criarHistorico(Lote lote, BigDecimal valorAlteracao) {
		BigDecimal valor = null;

		HistoricoAlteracaoValor historico = new HistoricoAlteracaoValor();
		historico.setObservacao(observacaoHistorico);
		historico.setUltimoValor(lote.getValor());
		historico.setData(new Date());
		historico.setCompra(compra);
		historico.setLote(lote);
		historico.setTipo(lote.getValor().compareTo(valorAlteracao) == -1 ? "ACRESCIMO" : "DESCONTO");
		historico.setPesoValorPeixe(novoValorKGPeixe != null ? lote.getValorUnitarioPeixe() : null);
		historico.setUsuario(new Usuario());
		historico.setValorDaCompra(compra.getValorTotal());
		historico.setUsuario(getUsuarioSession().getUsuario());

		if (historico.getTipo().equalsIgnoreCase("ACRESCIMO")) {
			valor = valorAlteracao.subtract(lote.getValor());
		} else {
			valor = lote.getValor().subtract(valorAlteracao);
		}

		historico.setValor(valor);
		if (historico.getTipo().equalsIgnoreCase("ACRESCIMO")) {
			lote.setAcrescimo(valor);
		} else {
			lote.setDesconto(valor);
		}

		compra.getHistoricos().add(historicoDAO.save(historico));
	}

	public void criarHistoricoAlteracaoNoTotal() {

		BigDecimal valor = null;
		HistoricoAlteracaoValor historico = new HistoricoAlteracaoValor();
		historico.setObservacao(observacaoHistorico);
		historico.setUltimoValor(compra.getValorTotal());
		historico.setValorDaCompra(compra.getValorTotal());
		historico.setData(new Date());
		historico.setCompra(compra);
		historico.setTipo(compra.getValorTotal().intValue() < valorAlterado.intValue() ? "ACRESCIMO" : "DESCONTO");
		historico.setUsuario(new Usuario());
		historico.setUsuario(getUsuarioSession().getUsuario());

		if (historico.getTipo().equalsIgnoreCase("ACRESCIMO")) {
			valor = valorAlterado.subtract(compra.getValorTotal());
		} else {
			valor = compra.getValorTotal().subtract(valorAlterado);
		}

		if (historico.getTipo().equalsIgnoreCase("ACRESCIMO")) {
			compra.setAcrescimo(valor);
		} else {
			compra.setDesconto(valor);
		}

		historico.setValor(valor);
		compra.getHistoricos().add(historicoDAO.save(historico));

	}

	public void testeCriarHis() {
		List<HistoricoAlteracaoValor> historicos = new ArrayList<HistoricoAlteracaoValor>();
		BigDecimal porcentagem = getPorcentagem();

		for (Lote l : compra.getLotes()) {

		}

	}

	public BigDecimal getPorcentagem() {

		BigDecimal valor = null;
		BigDecimal porcentagem = null;

		if (valorAlterado.intValue() > compra.getValorTotal().intValue()) {
			valor = compra.getValorTotal().subtract(valorAlterado);
			porcentagem = valor.multiply(new BigDecimal(100)).divide(
					compra.getValorTotal());
		} else {
			valor = compra.getValorTotal().subtract(valorAlterado);
			porcentagem = valor.multiply(new BigDecimal(100)).divide(
					compra.getValorTotal());
		}

		return porcentagem;

	}

	public void buscarAdiantamentosEpagamentos() {
		adiantamentos = new ArrayList<AdiantamentoFornecedor>();
		adiantamentos = adiantDao.getAdiantamentoPorFornecedor(compra.getFornecedor());

		chequesAdiantamento = new ArrayList<SelectItem>();

		List<AdiantamentoFornecedor> cheques = adiantDao.getChequesDisponiveisAdiantamento(compra.getFornecedor());
		for(AdiantamentoFornecedor a : cheques){
			chequesAdiantamento.add(new SelectItem(a, a.getNumeroCheque() + "/" + a.getNomeCheque() + " - R$ " + FormatterUtil.getValorFormatado(a.getValor())));
		}

		contasAdiantamento = new ArrayList<SelectItem>();

		List<AdiantamentoFornecedor> contas = adiantDao.getContasDisponiveisAdiantamento(compra.getFornecedor());
		for(AdiantamentoFornecedor a : contas){
			contasAdiantamento.add(new SelectItem(a, a.getConta().getAgencia() + "/" + a.getConta().getConta() + " - R$ " + FormatterUtil.getValorFormatado(a.getValor())));
		}

		pagamentos = new ArrayList<Pagamento>();
		pagamentos = pagDao.getPagamentoPorFornecedorEtipo(compra.getFornecedor());
	}

	public void calcularAdiantamento() {
		BigDecimal totalAd = BigDecimal.ZERO;
		BigDecimal totalPagamentos = BigDecimal.ZERO;

		for (AdiantamentoFornecedor ad : adiantamentos) {
			totalAd = totalAd.add(ad.getValor());
		}

		for (Pagamento pg : pagamentos) {
			totalPagamentos = totalPagamentos.add(pg.getValor());
		}

		totalAd = totalAd.subtract(totalPagamentos);

		totalAdiantamento = totalAd;

	}

	public List<HistoricoAlteracaoValor> buscarHistoricoDeAcrescimo() {

		BigDecimal totalPorPeso = BigDecimal.ZERO;
		BigDecimal totalPorvalor = BigDecimal.ZERO;
		Date dataUltimaAlteracao = null;
		List<HistoricoAlteracaoValor> lista = historicoDAO
				.getHistoricoPorTipoEcompra(compra);

		for (HistoricoAlteracaoValor historicoAlteracaoValor : lista) {
			if (historicoAlteracaoValor.getPesoValorPeixe() != null) {
				totalPorPeso = totalPorPeso.add(historicoAlteracaoValor
						.getValor());
			} else {
				totalPorvalor = totalPorPeso.add(historicoAlteracaoValor
						.getValor());
			}

			dataUltimaAlteracao = historicoAlteracaoValor.getData();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		if (totalPorPeso.compareTo(BigDecimal.ZERO) > 0) {
			observacaoAcrescimoPeso = new StringBuilder()
					.append("Foi alterado o peso do kg do peixe para um valor superior. Ultima alteração ")
					.append(sdf.format(dataUltimaAlteracao)).toString();
		} else {
			observacaoAcrescimoPeso = null;
		}

		if (totalPorvalor.compareTo(BigDecimal.ZERO) > 0) {
			observacaoAcrescimoValor = new StringBuilder()
					.append("Foram efetuados acréscimos ao valor da compra. Ultima alteração ")
					.append(sdf.format(dataUltimaAlteracao)).toString();
		} else {
			observacaoAcrescimoValor = null;
		}

		return lista.size() > 0 ? lista : null;
	}

	public void cancelarAlteracaoLote() {
		try {
			historicoDAO.removeAlteracaoLote(lote);
			
			lote.setValor(lote.getValor().subtract(lote.getAcrescimo()));
			lote.setValor(lote.getValor().add(lote.getDesconto()));
			lote.setValorUnitarioPeixe(lote.getPeixe().getValor());
			
			lote.setAcrescimo(BigDecimal.ZERO);
			lote.setDesconto(BigDecimal.ZERO);
			
			if (lote.getCompra() == null){
				lote.setCompra(new Compra());
				lote.setCompra(compra);
			}
			if (lote.getFornecedor() == null){
				lote.setFornecedor(new Fornecedor());
			    lote.setFornecedor(compra.getFornecedor());
			}
			
			loteDAO.save(lote);
			carregarDetalhesCompra();
			compra.setValorTotal(totalCompra);
			compraDAO.save(compra);
			addMessage("Informação", "Alteração cancelada com sucesso.",
					FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			addMessage("Erro",
					"Não foi possível cancelar a alteração.",
					FacesMessage.SEVERITY_ERROR);
		}

	}

	public void cancelarAlteracaoLotes() {

		this.lote.setTotalValorKG(BigDecimal.ZERO);
		this.lote.setTotalValor(BigDecimal.ZERO);
		this.lote.setTotalDesconto(BigDecimal.ZERO);
		this.lote.setTotalAcrescimo(BigDecimal.ZERO);

		for(Lote lote : compra.getLotes()) {

			if (this.lote.getPeixe().getDescricao().equals(lote.getPeixe().getDescricao())) {

				try {
					historicoDAO.removeAlteracaoLote(lote);

					lote.setValor(lote.getValor().subtract(lote.getAcrescimo()));
					lote.setValor(lote.getValor().add(lote.getDesconto()));
					lote.setValorUnitarioPeixe(lote.getPeixe().getValor());

					lote.setAcrescimo(BigDecimal.ZERO);
					lote.setDesconto(BigDecimal.ZERO);

					if (lote.getCompra() == null) {
						lote.setCompra(new Compra());
						lote.setCompra(compra);
					}
					if (lote.getFornecedor() == null) {
						lote.setFornecedor(new Fornecedor());
						lote.setFornecedor(compra.getFornecedor());
					}

					this.lote.setTotalValorKG(this.lote.getTotalValorKG().add(lote.getValorUnitarioPeixe()));
					this.lote.setTotalValor(this.lote.getTotalValor().add(lote.getValor()));
					this.lote.setValorUnitarioPeixe(lote.getValorUnitarioPeixe());

					loteDAO.save(lote);

				} catch (Exception e) {
					addMessage("Erro",
							"Não foi possível cancelar a alteração.",
							FacesMessage.SEVERITY_ERROR);
				}
			}
		}

		carregarDetalhesCompra();
		compra.setValorTotal(totalCompra);
		compraDAO.save(compra);
		addMessage("Informação", "Alterações canceladas com sucesso.",
				FacesMessage.SEVERITY_INFO);

	}
	
	public void cancelarAlteracaoTotal(){

		try{
			historicoDAO.removeAlteracaoTotal(compra);
			compra.setAcrescimo(BigDecimal.ZERO);
			compra.setDesconto(BigDecimal.ZERO);
			carregarDetalhesCompra();
			compra.setValorTotal(totalCompra);
			compraDAO.save(compra);
			addMessage("Informação", "Alteração cancelada com sucesso",
					FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro",
					"Não foi possível cancelar a alteração.",
					FacesMessage.SEVERITY_ERROR);
		}

	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public String prepararTelaCad() {
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

	public Integer getFiltroStatus() {
		return filtroStatus;
	}

	public void setFiltroStatus(Integer filtroStatus) {
		this.filtroStatus = filtroStatus;
	}

	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
	}

	public Integer getTipoAlteracaoValor() {
		return tipoAlteracaoValor;
	}

	public void setTipoAlteracaoValor(Integer tipoAlteracaoValor) {
		this.tipoAlteracaoValor = tipoAlteracaoValor;
	}

	public Integer getTipoValor() {
		return tipoValor;
	}

	public void setTipoValor(Integer tipoValor) {
		this.tipoValor = tipoValor;
	}

	public BigDecimal getNovoValorKGPeixe() {
		return novoValorKGPeixe;
	}

	public void setNovoValorKGPeixe(BigDecimal novoValorKGPeixe) {
		this.novoValorKGPeixe = novoValorKGPeixe;
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

	public BigDecimal getTotalCompra() {
		return totalCompra;
	}

	public void setTotalCompra(BigDecimal totalCompra) {
		this.totalCompra = totalCompra;
	}

	public BigDecimal getValorAlterado() {
		return valorAlterado;
	}

	public void setValorAlterado(BigDecimal valorAlterado) {
		this.valorAlterado = valorAlterado;
	}

	public Boolean getAlteracaoTotalCompra() {
		return alteracaoTotalCompra;
	}

	public void setAlteracaoTotalCompra(Boolean alteracaoTotalCompra) {
		this.alteracaoTotalCompra = alteracaoTotalCompra;
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

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public BigDecimal getTotalPago() {

		/*if (pagDinheiro == null)
			pagDinheiro = BigDecimal.ZERO;
		if (pagAdiantamentoDinheiro == null)
			pagAdiantamentoDinheiro = BigDecimal.ZERO;
		if (pagAdiantamentoCheque == null)
			pagAdiantamentoCheque = BigDecimal.ZERO;
		if (pagAdiantamentoTransferencia == null)
			pagAdiantamentoTransferencia = BigDecimal.ZERO;
		if (pagTransferencia == null)
			pagTransferencia = BigDecimal.ZERO;
		if (pagCheque == null)
			pagCheque = BigDecimal.ZERO;

		totalPago = BigDecimal.ZERO;
		totalPago = totalPago.add(pagDinheiro).add(pagAdiantamentoDinheiro).add(pagAdiantamentoCheque).add(pagAdiantamentoTransferencia)
				.add(pagTransferencia).add(pagCheque);*/

		totalPago = BigDecimal.ZERO;

		totalPago = pagamentos.stream().map(Pagamento::getValor).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

		return totalPago;
	}

	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}

	public String getMensagemConfirmacaoPagamento() {
		return mensagemConfirmacaoPagamento;
	}

	public void setMensagemConfirmacaoPagamento(
			String mensagemConfirmacaoPagamento) {
		this.mensagemConfirmacaoPagamento = mensagemConfirmacaoPagamento;
	}

	public BigDecimal getFaltaPagar() {
		if (faltaPagar != null) {
			faltaPagar = compra.getValorTotal().subtract(getTotalPago());
		}
		return faltaPagar;
	}

	public void setFaltaPagar(BigDecimal faltaPagar) {
		this.faltaPagar = faltaPagar;
	}

	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public List<AdiantamentoFornecedor> getAdiantamentos() {
		return adiantamentos;
	}

	public void setAdiantamentos(List<AdiantamentoFornecedor> adiantamentos) {
		this.adiantamentos = adiantamentos;
	}

	public BigDecimal getResultado() {
		return totalAdiantamento;
	}

	public void setResultado(BigDecimal resultado) {
		this.totalAdiantamento = resultado;
	}

	public Boolean getDisableButtonPagar() {
		return disableButtonPagar;
	}

	public void setDisableButtonPagar(Boolean disableButtonPagar) {
		this.disableButtonPagar = disableButtonPagar;
	}

	public String getCorConfirmacaoPagamento() {
		return corConfirmacaoPagamento;
	}

	public void setCorConfirmacaoPagamento(String corConfirmacaoPagamento) {
		this.corConfirmacaoPagamento = corConfirmacaoPagamento;
	}

	public Boolean getPreencheCampoTransferencia() {
		return preencheCampoTransferencia;
	}

	public void setPreencheCampoTransferencia(Boolean preencheCampoTransferencia) {
		this.preencheCampoTransferencia = preencheCampoTransferencia;
	}

	public Boolean getPreencheCampoCheque() {
		return preencheCampoCheque;
	}

	public void setPreencheCampoCheque(Boolean preencheCampoCheque) {
		this.preencheCampoCheque = preencheCampoCheque;
	}

	public Conta getConta() {
		if (conta == null) {
			conta = new Conta();
			conta.setFornecedor(new Fornecedor());
		}
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public StatusCompra[] getStatusCompra(){
		return StatusCompra.values();
	}

	public String getNomeCheque() {
		return nomeCheque;
	}

	public void setNomeCheque(String nomeCheque) {
		this.nomeCheque = nomeCheque;
	}

	public Date getDataCheque() {
		return dataCheque;
	}

	public void setDataCheque(Date dataCheque) {
		this.dataCheque = dataCheque;
	}

	public String getMsgCampoTransferencia() {
		return msgCampoTransferencia;
	}

	public void setMsgCampoTransferencia(String msgCampoTransferencia) {
		this.msgCampoTransferencia = msgCampoTransferencia;
	}

	public String getMsgCampoCheque() {
		return msgCampoCheque;
	}

	public void setMsgCampoCheque(String msgCampoCheque) {
		this.msgCampoCheque = msgCampoCheque;
	}

	public Boolean getMostrarMensagem() {
		return mostrarMensagem;
	}

	public void setMostrarMensagem(Boolean mostrarMensagem) {
		this.mostrarMensagem = mostrarMensagem;
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

	public List<SelectItem> getListaStatus() {
		return listaStatus;
	}

	public void setListaStatus(List<SelectItem> listaStatus) {
		this.listaStatus = listaStatus;
	}

	public Observacao getObservacao() {
		return observacao;
	}

	public void setObservacao(Observacao observacao) {
		this.observacao = observacao;
	}

	public String getObservacaoHistorico() {
		return observacaoHistorico;
	}

	public void setObservacaoHistorico(String observacaoHistorico) {
		this.observacaoHistorico = observacaoHistorico;
	}

	public Boolean getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(Boolean autorizado) {
		this.autorizado = autorizado;
	}

	public BigDecimal getValorMinimoAdiantamento() {
		return valorMinimoAdiantamento;
	}

	public void setValorMinimoAdiantamento(BigDecimal valorMinimoAdiantamento) {
		this.valorMinimoAdiantamento = valorMinimoAdiantamento;
	}

	public List<SelectItem> getListaDecontas() {
		return listaDecontas;
	}

	public void setListaDecontas(List<SelectItem> listaDecontas) {
		this.listaDecontas = listaDecontas;
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
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

	public String getNomeBarco() {
		return nomeBarco;
	}

	public void setNomeBarco(String nomeBarco) {
		this.nomeBarco = nomeBarco;
	}

	public List<Barco> getBarcos() {
		return barcos;
	}

	public void setBarcos(List<Barco> barcos) {
		this.barcos = barcos;
	}

	public List<SelectItem> getChequesAdiantamento() {
		return chequesAdiantamento;
	}

	public void setChequesAdiantamento(List<SelectItem> chequesAdiantamento) {
		this.chequesAdiantamento = chequesAdiantamento;
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

	public List<SelectItem> getContasAdiantamento() {
		return contasAdiantamento;
	}

	public void setContasAdiantamento(List<SelectItem> contasAdiantamento) {
		this.contasAdiantamento = contasAdiantamento;
	}

	public String getMsgCampoTransferenciaAdiantamento() {
		return msgCampoTransferenciaAdiantamento;
	}

	public void setMsgCampoTransferenciaAdiantamento(String msgCampoTransferenciaAdiantamento) {
		this.msgCampoTransferenciaAdiantamento = msgCampoTransferenciaAdiantamento;
	}

	public String getMsgCampoChequeAdiantamento() {
		return msgCampoChequeAdiantamento;
	}

	public void setMsgCampoChequeAdiantamento(String msgCampoChequeAdiantamento) {
		this.msgCampoChequeAdiantamento = msgCampoChequeAdiantamento;
	}

	public Boolean getPreencheCampoTranferenciaAdiantamento() {
		return preencheCampoTranferenciaAdiantamento;
	}

	public void setPreencheCampoTranferenciaAdiantamento(Boolean preencheCampoTranferenciaAdiantamento) {
		this.preencheCampoTranferenciaAdiantamento = preencheCampoTranferenciaAdiantamento;
	}

	public Boolean getPreencheCampoChequeAdiantamento() {
		return preencheCampoChequeAdiantamento;
	}

	public void setPreencheCampoChequeAdiantamento(Boolean preencheCampoChequeAdiantamento) {
		this.preencheCampoChequeAdiantamento = preencheCampoChequeAdiantamento;
	}

	//Utilizados na nova forma de pagamento dinamico
	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public String getTipoDoPagamento() {
		return tipoDoPagamento;
	}

	public void setTipoDoPagamento(String tipoDoPagamento) {
		this.tipoDoPagamento = tipoDoPagamento;
	}

	public TipoAdiantamento[] getTiposAdiantamento(){
		return TipoAdiantamento.values();
	}

	public AdiantamentoFornecedor getAdiantamentoPagamento() {
		return adiantamentoPagamento;
	}

	public void setAdiantamentoPagamento(AdiantamentoFornecedor adiantamentoPagamento) {
		this.adiantamentoPagamento = adiantamentoPagamento;
	}

	public TipoAdiantamento getTipoAdiantamentoPagamento() {
		return tipoAdiantamentoPagamento;
	}

	public void setTipoAdiantamentoPagamento(TipoAdiantamento tipoAdiantamentoPagamento) {
		this.tipoAdiantamentoPagamento = tipoAdiantamentoPagamento;
	}

	public StatusCompra getStatus() {
		return status;
	}

	public void setStatus(StatusCompra status) {
		this.status = status;
	}
}