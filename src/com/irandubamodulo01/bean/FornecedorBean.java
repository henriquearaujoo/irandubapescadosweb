package com.irandubamodulo01.bean;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.irandubamodulo01.dao.*;
import com.irandubamodulo01.enumerated.TipoAdiantamento;
import com.irandubamodulo01.enumerated.TipoPessoa;
import com.irandubamodulo01.model.*;
import com.irandubamodulo01.util.ReportUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.irandubamodulo01.util.Filtro;

@Named(value="fornecedorBean")
@javax.faces.view.ViewScoped
public class FornecedorBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private @Inject Fornecedor fornec; 
	private @Inject FornecedorDAO fornDAO;
	private @Inject BarcoDAO barcoDAO;
	private @Inject AdiantamentoDAO adiantamentoDAO;
	private @Inject ContaDAO contaDAO;
	private @Inject	PrecoDiferenciadoDAO precoDiferenciadoDAO;
	private @Inject PeixeDAO peixeDAO;
	private @Inject PagamentoDAO pagamentoDAO;
	private @Inject ArquivoDAO arquivoDAO;
	private  LazyDataModel<Fornecedor> model;
	private LazyDataModel<Barco> modelBarcos;
	private Filtro filtro = new Filtro();
	private Barco barco = new Barco();
	private AdiantamentoFornecedor adiantamento = new  AdiantamentoFornecedor();
	private Conta conta;
	private String nomePeixe;
	private String nomeFornecedor;
	private List<Peixe> peixes;
	private List<Fornecedor> fornecedores;
	private PrecoDiferenciado precoDiferenciado = new PrecoDiferenciado();
	private BigDecimal totalAdiantamento = BigDecimal.ZERO;
	private BigDecimal saldoDevedor = BigDecimal.ZERO;
	private List<SelectItem> listaDeContas;
	private Part comprovante;
	private @Inject Arquivo arquivo;
	private List<Pagamento> descontosAdiantamentos;
	private BigDecimal totalDescontosAdiantamento = BigDecimal.ZERO;

	public FornecedorBean() {
		
		model = new LazyDataModel<Fornecedor>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<Fornecedor> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				setRowCount(fornDAO.quantidadeFiltrados(filtro));
				return fornDAO.filtrados(filtro);
			}
		};
	}
	
	public void carregarBarcos(){
		if (fornec != null) {
			if (fornec.getId() != null) {
				fornec.setBarcos(new ArrayList<Barco>());
				fornec.getBarcos().addAll(barcoDAO.getBarcosPorFornecedor(fornec));
			}
		}
	}
	
	public void carregarContas(){
		if (fornec != null) {
			if (fornec.getId() != null) {
				fornec.setContas(contaDAO.getContasPorFornecedor(fornec));

				listaDeContas = new ArrayList<SelectItem>();

				for (Conta conta : fornec.getContas()) {
					listaDeContas.add(new SelectItem(conta, conta.getAgencia() + "/"
							+ conta.getConta()));
				}
			}
		}

	}
	
	public void carregarAdiantamentos(){
		if (fornec != null) {
			if (fornec.getId() != null) {
				fornec.setAdiantamentos(adiantamentoDAO.getAdiantamentoPorFornecedor(fornec));	
			}
		}
	}

	public void carregarPrecosDiferenciados(){
		if (fornec != null){
			if (fornec.getId() != null){
				fornec.setPrecosDiferenciados(precoDiferenciadoDAO.getPrecoDiferenciadoPorFornecedor(fornec));
			}
		}
	}

	public void carregarDados(){
		setPeixes(peixeDAO.getPeixesComplete());
		setFornecedores(fornDAO.getFornecedoresComplete());
	}

	public void cancelarCadBarco(){
		barco = new Barco();
	}
	
	public void cancelarCadAdiantamento(){
		adiantamento = new AdiantamentoFornecedor();
	}
	
	public void cancelarCadConta(){
		conta = new Conta();
	}
	
	public void prepararCadastroBarco(){
		barco = new Barco();
	}
	
	public void prepararAdiantamento(){
		adiantamento = new AdiantamentoFornecedor();
		carregarContas();
	}

	public void editarAdiantamento(){
		carregarContas();

		List<Arquivo> arquivos = arquivoDAO.getArquivosPorAdiantamento(adiantamento);

		if (arquivos.size() > 0)
			arquivo = arquivos.get(0);
		else
			arquivo = null;
	}
	
	public void prepararCadastroConta(){
		conta = new Conta();
	}
	
	public TipoConta[] getTiposConta(){
		return TipoConta.values();
	}

	public TipoPessoa[] getTiposPessoa(){
		return TipoPessoa.values();
	}

	public TipoAdiantamento[] getTiposAdiantamento () { return TipoAdiantamento.values(); }
	
	public void excluirBarco(){
		try {
			barcoDAO.remove(barco);
			addMessage("Informação: ", "Transporte excluido com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			addMessage("Erro: ", "Não foi possivel excluir o transporte.", FacesMessage.SEVERITY_ERROR);
		}
		
		carregarBarcos();
	}
	
	public void excluirAdiantamento(){
		try {
			List<Arquivo> list = arquivoDAO.getArquivosPorAdiantamento(adiantamento);
			for(Arquivo arquivo : list){
				arquivoDAO.remove(arquivo);
			}

			adiantamentoDAO.remove(adiantamento);
			addMessage("Informação: ", "Adiantemento excluido com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("Erro: ", "Não foi possivel excluir o adiantamento.", FacesMessage.SEVERITY_ERROR);
		}
		
		carregarAdiantamentos();
	}
	
	public void excluirConta(){
		try {
			contaDAO.remove(conta);
			addMessage("Informação: ", "Conta excluida com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("Erro: ", "Não foi possivel excluir a conta.", FacesMessage.SEVERITY_ERROR);
		}
		
		carregarContas();
	}

	public void excluirPrecoDiferenciado(){
		try{
			precoDiferenciadoDAO.remove(precoDiferenciado);
			addMessage("Informação: ", "Item excluido com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro: ", "Não foi possivel excluir o item.", FacesMessage.SEVERITY_ERROR);
		}

		carregarPrecosDiferenciados();
	}
	
	public void salvarBarco(){
		if (barco.getNome() != null && !barco.getNome().equals("")) {
			barco.setFornecedor(fornec);
			barcoDAO.save(barco);
			fornec.setBarcos(new ArrayList<Barco>());
			fornec.getBarcos().addAll(barcoDAO.getBarcosPorFornecedor(fornec));
			barco = new Barco();
			addMessage("Informação: ", "Transporte salvo com sucesso.", FacesMessage.SEVERITY_INFO);
		}else
			addMessage("Não foi possível salvar o transporte.", "Preencha os campos obrigatórios.", FacesMessage.SEVERITY_ERROR);
	}

	public UsuarioSessionBean getUsuarioSession(){
		UsuarioSessionBean usuarioSession = (UsuarioSessionBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuariologado");
		return usuarioSession;
	}

	private String getFileName(Part part) {
		final String partHeader = part.getHeader("content-disposition");
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		return null;
	}

	public String salvarComprovante(){
		// Extract file name from content-disposition header of file part
		String fileName = getFileName(comprovante);
		fileName = adiantamento.getId() + "_" + fileName;

		String basePath = "C:" + File.separator + "Iranduba Pescados" + File.separator + "arquivos" + File.separator + "comprovantes" + File.separator;
		File outputFilePath = new File(basePath + fileName);

		// Copy uploaded file to destination path
		java.io.InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = comprovante.getInputStream();
			outputStream = new FileOutputStream(outputFilePath);

			int read = 0;
			final byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			List<Arquivo> list = arquivoDAO.getArquivosPorAdiantamento(adiantamento);
			Arquivo arquivo1 = null;
			if (list != null && list.size() > 0) {
				arquivo1 = list.get(0);

				File fotoAnterior = new File(basePath + arquivo1.getNome());
				if (fotoAnterior.exists())
					fotoAnterior.delete();
			}else
				arquivo1 = new Arquivo();

			arquivo1.setAdiantamento(adiantamento);
			arquivo1.setNome(fileName);
			arquivo1.setTamanho(comprovante.getSize());
			arquivo1.setTipo(comprovante.getContentType());

			arquivoDAO.save(arquivo1);

			//carregarDetalhes();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}

	public void salvarAdiantamento(){
		try {
			if (adiantamento.getValor() != null && adiantamento.getValor().compareTo(BigDecimal.ZERO) == 1 && adiantamento.getObservacao() != null && !adiantamento.getObservacao().isEmpty()) {

				if (adiantamento.getTipo().equals(TipoAdiantamento.TRANSFERENCIA)) {
					if (adiantamento.getConta() == null && comprovante == null) {
						addMessage("Aviso", "Selecione uma conta e adicione um comprovante.", FacesMessage.SEVERITY_ERROR);
						return;
					} else {
						adiantamento.setNomeCheque(null);
						adiantamento.setNumeroCheque(null);
					}
				}

				if (adiantamento.getTipo().equals(TipoAdiantamento.CHEQUE)) {
					if ((adiantamento.getNomeCheque() == null || adiantamento.getNomeCheque().isEmpty()) || (adiantamento.getNumeroCheque() == null || adiantamento.getNumeroCheque().isEmpty())) {
						addMessage("Aviso", "Preencha o nome e o número do cheque.", FacesMessage.SEVERITY_ERROR);
						return;
					} else {
						adiantamento.setConta(null);
					}
				}

				adiantamento.setFornecedor(fornec);
				adiantamento.setUsuario(getUsuarioSession().getUsuario());
				adiantamento.setData(new Date());
				adiantamento = adiantamentoDAO.save(adiantamento);
				if(comprovante != null)
					salvarComprovante();
				fornec.setAdiantamentos(new ArrayList<AdiantamentoFornecedor>());
				fornec.getAdiantamentos().addAll(adiantamentoDAO.getAdiantamentoPorFornecedor(fornec));
				adiantamento = new AdiantamentoFornecedor();
				addMessage("Informação", "Adiantamento salvo com sucesso.", FacesMessage.SEVERITY_INFO);
			} else
				addMessage("Aviso", "Preencha os campos obrigatórios.", FacesMessage.SEVERITY_ERROR);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível salvar o adiantamento.", FacesMessage.SEVERITY_ERROR);
		}
	}

	public void downloadComprovante(){

		try{

			if (arquivo != null){
				arquivo = arquivoDAO.getById(Arquivo.class, arquivo.getId());

				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=\"" + arquivo.getNome() + "\"");
				File file = new File("C:" + File.separator + "Iranduba Pescados" + File.separator + "arquivos" + File.separator + "comprovantes" + File.separator + arquivo.getNome());
				response.getOutputStream().write(IOUtils.toByteArray(new FileInputStream(file)));
				response.getOutputStream().flush();
				response.getOutputStream().close();
				FacesContext.getCurrentInstance().responseComplete();
			}else{
				addMessage("Aviso: ", "Esse pagamento não possui comprovante.", FacesMessage.SEVERITY_WARN);
			}

		}catch (Exception e){
			e.printStackTrace();
		}

		arquivo.setDados(null);

	}

	
	public void salvarConta(){
		if ((conta.getBanco() != null && !conta.getBanco().equals("")) && (conta.getBeneficiado() != null && !conta.getBeneficiado().equals("")) && (conta.getAgencia() != null && !conta.getAgencia().equals("") && (conta.getConta() != null && !conta.getConta().equals("")))){
			conta.setFornecedor(fornec);
			contaDAO.save(conta);
			fornec.setContas(new ArrayList<Conta>());
			fornec.getContas().addAll(contaDAO.getContasPorFornecedor(fornec));
			conta = new Conta();
			addMessage("Informação: ", "Conta salva com sucesso.", FacesMessage.SEVERITY_INFO);
		}else
			addMessage("Não foi possível salvar a conta.", "Preencha os campos obrigatórios.", FacesMessage.SEVERITY_ERROR);
	}
	
	public String cancelar(){
		return "fornecedores?faces-redirect=true";
	}
	
	public void salvarFornec(){

		try {
			fornec = fornDAO.save(fornec);
			addMessage("Informação: ", "Fornecedor salvo com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("Erro: ", "Não foi possível salvar fornecedor.", FacesMessage.SEVERITY_ERROR);
		}
	}
	
	public String prepararTelaCad(){
		return "forneccad?faces-redirect=true";
	}
	

	
	public String editarFornec(){
		return "forneccad?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public void deletarFornec(){
		if (fornDAO.verificaExisteCompra(fornec)) {
			addMessage("Não foi possivel deletar fornecedor", "fornecedor associado a uma compra.", FacesMessage.SEVERITY_ERROR);
			fornec = new Fornecedor();
			return;
		}
		try{
			fornDAO.remove(fornec);
			addMessage("Informação", "Fornecedor excluído com sucesso", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível excluir o fornecedor.", FacesMessage.SEVERITY_ERROR);
		}

	}

	public List<String> completePeixe(String query){
		List<String> results = new ArrayList<String>();
		for (Peixe peixe : getPeixes()){
			if (peixe.getDescricao().toLowerCase().startsWith(query.toLowerCase()))
				results.add(peixe.getDescricao());
		}
		return results;
	}

	public List<String> completeFornecedor(String query){
		List<String> results = new ArrayList<String>();
		for (Fornecedor fornecedor : getFornecedores()){
			if (fornecedor.getNome().toLowerCase().startsWith(query.toLowerCase()))
				results.add(fornecedor.getNome());
		}
		return results;
	}

	public void adicionarPrecoDiferenciado(){
		if (nomePeixe != null && !nomePeixe.isEmpty() && precoDiferenciado.getValor() != null && !precoDiferenciado.getValor().toString().isEmpty()) {

			try {
				Optional<Peixe> peixe = peixes.stream().filter(p -> p.getDescricao().equals(nomePeixe)).findFirst();

				PrecoDiferenciado pd = precoDiferenciadoDAO.getPrecoDiferenciadoPorFornecedorEPeixe(fornec, peixe.get());

				if (pd == null) {
					precoDiferenciado.setPeixe(peixe.get());
					precoDiferenciado.setFornecedor(fornec);

					precoDiferenciadoDAO.save(precoDiferenciado);

					precoDiferenciado = new PrecoDiferenciado();
					nomePeixe = null;
					carregarPrecosDiferenciados();

					addMessage("Informação", "Valor adicionado com sucesso.", FacesMessage.SEVERITY_INFO);
				}
				else
					addMessage("Advertência", "Já foi adicionado um valor para o peixe selecionado.", FacesMessage.SEVERITY_WARN);
			} catch (Exception e) {
				e.printStackTrace();
				addMessage("Erro", "Não foi possível adicionar o valor.", FacesMessage.SEVERITY_ERROR);
			}
		}else
			addMessage("Advertência", "Selecione um peixe e digite um valor antes de adicionar um novo item.", FacesMessage.SEVERITY_WARN);
	}

	public void copiarPrecosDiferenciados(){
		if (nomeFornecedor != null && !nomeFornecedor.isEmpty()) {
			try {
				List<Long> ids = new ArrayList<Long>();
				Optional<Fornecedor> fornecedor = fornecedores.stream().filter(f -> f.getNome().equals(nomeFornecedor)).findFirst();
				for (PrecoDiferenciado pd : fornec.getPrecosDiferenciados()) {
					ids.add(pd.getPeixe().getId());
				}
				List<PrecoDiferenciado> list = precoDiferenciadoDAO.getPrecoDiferenciadoPorFornecedorPeixeDif(fornecedor.get(), ids);

				for (PrecoDiferenciado pd : list) {
					pd.setId(null);
					pd.setFornecedor(fornec);

					precoDiferenciadoDAO.save(pd);
				}

				nomeFornecedor = null;
				carregarPrecosDiferenciados();

				addMessage("Informação", "Cópia dos itens efetuada com sucesso.", FacesMessage.SEVERITY_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				addMessage("Erro", "Não foi possível efetuar a cópia dos itens.", FacesMessage.SEVERITY_ERROR);
			}
		}else
			addMessage("Advertência", "Selecione um fornecedor antes de efetuar a cópia.", FacesMessage.SEVERITY_WARN);
	}

	public void atualizarValores(){
		try {

			for(PrecoDiferenciado pd : fornec.getPrecosDiferenciados()){
				precoDiferenciadoDAO.save(pd);
			}

			carregarPrecosDiferenciados();

			addMessage("Informação", "Valores atualizados com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível atualizar os valores dos itens.", FacesMessage.SEVERITY_ERROR);
		}
	}

	public void limpar(){

		fornec = new Fornecedor();
	}

	public BigDecimal getTotalAdiantamento(){
		totalAdiantamento = BigDecimal.ZERO;

		if (fornec.getAdiantamentos() != null)
			totalAdiantamento = fornec.getAdiantamentos().stream().map(AdiantamentoFornecedor::getValor).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

		return totalAdiantamento;
	}

	public BigDecimal getSaldoDevedor() {
		saldoDevedor = BigDecimal.ZERO;

		BigDecimal totalPagamentosPorFornecedor = pagamentoDAO.getPagamentoPorFornecedorEtipo(fornec).stream().map(Pagamento::getValor).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

		saldoDevedor = getTotalAdiantamento().subtract(totalPagamentosPorFornecedor);

		return saldoDevedor;
	}

	public void obterDescontosDeAdiantamento(){
		descontosAdiantamentos = pagamentoDAO.obterDescontosDeAdiantamentoFornecedor(fornec);
	}

	public void imprimirTabelaPrecoFornecedor() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");

		Map parametros = new HashMap();

		parametros.put("nomefornecedor", fornec.getNome());
		parametros.put("filtro_consulta", "fornecedor_id = " + fornec.getId());

		try {
			JasperDesign jd = null;

			String nomeRel = "relatorio_preco_fornecedor_";
			jd = JRXmlLoader.load(path + "relatorios/relatorio_preco_diferenciado_fornecedor.jrxml");

			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReportCon("Relatório", nomeRel + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()), report, parametros, fornDAO.getConnection(), "PDF");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addMessage(String summary, String detail, Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	public List<Peixe> getPeixes() {
		return peixes;
	}

	public void setPeixes(List<Peixe> peixes) {
		this.peixes = peixes;
	}
	
	public Fornecedor getFornec() {
		return fornec;
	}
	public void setFornec(Fornecedor fornec) {
		this.fornec = fornec;
	}
	

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public LazyDataModel<Fornecedor> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Fornecedor> model) {
		this.model = model;
	}


	public Barco getBarco() {
		return barco;
	}


	public void setBarco(Barco barco) {
		this.barco = barco;
	}

	public LazyDataModel<Barco> getModelBarcos() {
		return modelBarcos;
	}

	public void setModelBarcos(LazyDataModel<Barco> modelBarcos) {
		this.modelBarcos = modelBarcos;
	}

	public AdiantamentoFornecedor getAdiantamento() {
		return adiantamento;
	}

	public void setAdiantamento(AdiantamentoFornecedor adiantamento) {
		this.adiantamento = adiantamento;
	}

	public Conta getConta() {
		if (conta == null)
			conta = new Conta();
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public String getNomePeixe() {
		return nomePeixe;
	}

	public void setNomePeixe(String nomePeixe) {
		this.nomePeixe = nomePeixe;
	}

	public PrecoDiferenciado getPrecoDiferenciado() {
		return precoDiferenciado;
	}

	public void setPrecoDiferenciado(PrecoDiferenciado precoDiferenciado) {
		this.precoDiferenciado = precoDiferenciado;
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public void setTotalAdiantamento(BigDecimal totalAdiantamento) {
		this.totalAdiantamento = totalAdiantamento;
	}

	public void setSaldoDevedor(BigDecimal saldoDevedor) {
		this.saldoDevedor = saldoDevedor;
	}

	public List<SelectItem> getListaDeContas() {
		return listaDeContas;
	}

	public void setListaDeContas(List<SelectItem> listaDeContas) {
		this.listaDeContas = listaDeContas;
	}

	public Part getComprovante() {
		return comprovante;
	}

	public void setComprovante(Part comprovante) {
		this.comprovante = comprovante;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public List<Pagamento> getDescontosAdiantamentos() {
		return descontosAdiantamentos;
	}

	public void setDescontosAdiantamentos(List<Pagamento> descontosAdiantamentos) {
		this.descontosAdiantamentos = descontosAdiantamentos;
	}

	public BigDecimal getTotalDescontosAdiantamento() {

		totalDescontosAdiantamento = BigDecimal.ZERO;

		if (descontosAdiantamentos != null && descontosAdiantamentos.size() > 0)
			totalDescontosAdiantamento = descontosAdiantamentos.stream().map(Pagamento::getValor).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

		return totalDescontosAdiantamento;
	}

	public void setTotalDescontosAdiantamento(BigDecimal totalDescontosAdiantamento) {
		this.totalDescontosAdiantamento = totalDescontosAdiantamento;
	}
}
