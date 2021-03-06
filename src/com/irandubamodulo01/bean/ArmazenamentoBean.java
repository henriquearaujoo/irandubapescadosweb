package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.irandubamodulo01.dao.*;
import com.irandubamodulo01.model.*;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.irandubamodulo01.enumerated.StatusArmazenamento;
import com.irandubamodulo01.util.Filtro;

@Named(value = "armazenamentoBean")
@ViewScoped
public class ArmazenamentoBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject ArmazenamentoDAO armazenamentoDao;
	private @Inject Armazenamento armazenamento;
	private @Inject ObservacaoArmazenamentoDAO observacaoArmazenamentoDAO;
	private Filtro filtro =  new Filtro();
	private LazyDataModel<Armazenamento> model;
	private Usuario usuario;
    private List<SelectItem> listaStatus;
	private ObservacaoArmazenamento observacao = new ObservacaoArmazenamento();


	//Variaveis temporarias para cadastro de armazenamento
	private Boolean inclusaoContinua = true;
	private String nomePeixe;
	private List<Peixe> peixes;
	private @Inject PeixeDAO peixeDAO;
	private @Inject TipoPeixeDAO tipoPeixeDAO;
	private @Inject TamanhoPeixeDAO tamanhoPeixeDAO;
	private @Inject EmbalagemDAO embalagemDAO;
	private @Inject CamaraDAO camaraDAO;
	private @Inject PosicaoCamaraDAO posicaoCamaraDAO;
	private List<SelectItem> tiposPeixe;
	private List<SelectItem> tamanhosPeixe;
	private List<SelectItem> embalagens;
	private List<SelectItem> camaras;
	private List<SelectItem> posicoes;

	public ArmazenamentoBean(){
		usuario = getUsuarioSession().getUsuario();
		carregarFiltroStatus();
		model = new LazyDataModel<Armazenamento>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<Armazenamento> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setUsuario(usuario);
				setRowCount(armazenamentoDao.quantidadeFiltrados(filtro));
				return armazenamentoDao.filtrados(filtro);
			}
		};
		
	}
	
	public void carregarFiltroStatus() {
		listaStatus = new ArrayList<SelectItem>();
		for (StatusArmazenamento status : StatusArmazenamento.values()) {
			if (!usuario.getPerfil().getDescricao().equals("Admin") && status.getStatus().equalsIgnoreCase("RETORNADO")) 
			 {continue;}
			 listaStatus.add(new SelectItem(status,status.getStatus()));
		}
	}

	public String redirecionarDetalhes(){
		return  "detalhes_armazenamento?faces-redirect=true&amp;includeViewParams=true";
	}

	public String retornar(){
		try {
		 observacao.setData(new Date());
	     observacao.setUsuario(getUsuarioSession().getUsuario());
	     observacao.setArmazenamento(armazenamento);
		 armazenamento.setStatus(StatusArmazenamento.RETORNADO);
		 armazenamentoDao.save(armazenamento);
		 observacaoArmazenamentoDAO.save(observacao);
		 addMessage("Retornado", "Armazenamento retornado para correção", FacesMessage.SEVERITY_INFO);
		 return "armazenamento?faces-redirect=true";
		} catch (Exception e) {
		 e.printStackTrace();	
	     addMessage("Erro", "Erro ao estornar entre em contato com suporte técnico", FacesMessage.SEVERITY_ERROR);
		 return null;
		}
	}
	
	public String autorizar(){
		try {
		 observacao.setData(new Date());
		 observacao.setUsuario(getUsuarioSession().getUsuario());
		 observacao.setArmazenamento(armazenamento);
		 armazenamento.setStatus(StatusArmazenamento.AUTORIZADO);
		 armazenamentoDao.save(armazenamento);
		 observacaoArmazenamentoDAO.save(observacao);
		 // o armazenamento ja � o pr�prio estoque ??? ou ser� necess�rio outra tabela ??? tamb�m n�o tenho a tabela retirada
		 addMessage("Autorizado", "Armazenamento em estoque", FacesMessage.SEVERITY_INFO);
		 return "armazenamento?faces-redirect=true";
		} catch (Exception e) {
	     addMessage("Erro", "Erro ao autorizar entre em contato com suporte técnico", FacesMessage.SEVERITY_INFO);
		 return  null;
		}
	}

	public UsuarioSessionBean getUsuarioSession(){
		UsuarioSessionBean usuarioSession = (UsuarioSessionBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuariologado");
		return usuarioSession;
	}
	
	public void addMessage(String summary, String detail, Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	//Metodos temporarios para cadastro de armazenamto

	public String prepararTelaCad(){
		return  "armazenamentocad?faces-redirect=true";
	}

	public List<String> completePeixe(String query){
		List<String> results = new ArrayList<String>();
		for (Peixe peixe : getPeixes()){
			if (peixe.getDescricao().toLowerCase().startsWith(query.toLowerCase()))
				results.add(peixe.getDescricao());
		}
		return results;
	}

	public void inicializarArmazenamento(){
		setPeixes(peixeDAO.getPeixesCompleteVenda());

		carregarTipos();
		carregarTamanhos();
		carregarEmbalagens();
		carregarCamaras();
		carregarPosicoes2();

		if (getArmazenamento().getId() == null) {
			getArmazenamento().setQtdeEmbalagem(1);
		}else{
			nomePeixe = getArmazenamento().getPeixe().getDescricao();
		}
	}

	public void carregarTipos(){
		List<TipoPeixe> tipoPeixes = tipoPeixeDAO.getTiposComplete();

		this.tiposPeixe = new ArrayList<SelectItem>();

		for (TipoPeixe p : tipoPeixes){
			this.tiposPeixe.add(new SelectItem(p, p.getDescricao()));
		}
	}

	public void carregarTamanhos(){
		List<TamanhoPeixe> tamanhos = tamanhoPeixeDAO.getTamanhosComplete();

		this.tamanhosPeixe = new ArrayList<SelectItem>();

		for (TamanhoPeixe p : tamanhos){
			this.tamanhosPeixe.add(new SelectItem(p, p.getDescricao()));
		}
	}

	public void carregarEmbalagens(){
		List<Embalagem> embalagems = embalagemDAO.getEmbalagensComplete();

		this.embalagens = new ArrayList<SelectItem>();

		for (Embalagem p : embalagems){
			this.embalagens.add(new SelectItem(p, p.getDescricao()));
		}
	}

	public void carregarCamaras(){
		List<Camara> camaras = camaraDAO.getCamarasComplete();

		this.camaras = new ArrayList<SelectItem>();

		for (Camara p : camaras){
			this.camaras.add(new SelectItem(p, p.getDescricao()));
		}
	}

	public void carregarPosicoes(){
		if (armazenamento != null && armazenamento.getCamara() != null) {
			List<PosicaoCamara> posicoes = posicaoCamaraDAO.getPosicoesByCamara(armazenamento.getCamara());

			this.posicoes = new ArrayList<SelectItem>();

			for (PosicaoCamara p : posicoes){
				this.posicoes.add(new SelectItem(p, p.getDescricao()));
			}
		}
	}

	public void carregarPosicoes2(){
		List<PosicaoCamara> posicoes = posicaoCamaraDAO.getPosicoesComplete();

		this.posicoes = new ArrayList<SelectItem>();

		for (PosicaoCamara p : posicoes){
			this.posicoes.add(new SelectItem(p, p.getDescricao()));
		}
	}

	public String salvar(){
		try{
			getArmazenamento().setPeixe(peixeDAO.getPeixePorDescricao(nomePeixe));
			getArmazenamento().setStatus(StatusArmazenamento.AUTORIZADO);
			//getArmazenamento().setData(new Date());
			getArmazenamento().setUsuario(getUsuarioSession().getUsuario());
			setArmazenamento(armazenamentoDao.save(getArmazenamento()));
			addMessage("Informação", "Armazenamento salvo com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch(Exception e){
			addMessage("Erro", "Não foi possível salvar o armazenamento.", FacesMessage.SEVERITY_ERROR);
		}

		if (getInclusaoContinua()) {
			armazenamento = new Armazenamento();
			armazenamento.setQtdeEmbalagem(1);
			nomePeixe = null;
			return "";
		}else
			return cancelar();
	}

	public String cancelar(){
		return "armazenamento?faces-redirect=true";
	}

	public String editar(){
		return "armazenamentocad?faces-redirect=true&amp;includeViewParams=true";
	}

	public void delete(){
		try {
			armazenamentoDao.remove(getArmazenamento());
			addMessage("Informação", "Armazenamento excluído com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível excluir o armazenamento.", FacesMessage.SEVERITY_ERROR);
		}

	}

	public Armazenamento getArmazenamento() {
		return armazenamento;
	}

	public void setArmazenamento(Armazenamento armazenamento) {
		this.armazenamento = armazenamento;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public LazyDataModel<Armazenamento> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Armazenamento> model) {
		this.model = model;
	}

	public List<SelectItem> getListaStatus() {
		return listaStatus;
	}

	public void setListaStatus(List<SelectItem> listaStatus) {
		this.listaStatus = listaStatus;
	}

	public ObservacaoArmazenamento getObservacao() {
		return observacao;
	}

	public void setObservacao(ObservacaoArmazenamento observacao) {
		this.observacao = observacao;
	}

	//Metodos temporarios para cadastro de armazenamto
	public Boolean getInclusaoContinua() {
		return inclusaoContinua;
	}

	public void setInclusaoContinua(Boolean inclusaoContinua) {
		this.inclusaoContinua = inclusaoContinua;
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

	public List<SelectItem> getTiposPeixe() {
		return tiposPeixe;
	}

	public void setTiposPeixe(List<SelectItem> tiposPeixe) {
		this.tiposPeixe = tiposPeixe;
	}

	public List<SelectItem> getTamanhosPeixe() {
		return tamanhosPeixe;
	}

	public void setTamanhosPeixe(List<SelectItem> tamanhosPeixe) {
		this.tamanhosPeixe = tamanhosPeixe;
	}

	public List<SelectItem> getEmbalagens() {
		return embalagens;
	}

	public void setEmbalagens(List<SelectItem> embalagens) {
		this.embalagens = embalagens;
	}

	public List<SelectItem> getCamaras() {
		return camaras;
	}

	public void setCamaras(List<SelectItem> camaras) {
		this.camaras = camaras;
	}

	public List<SelectItem> getPosicoes() {
		return posicoes;
	}

	public void setPosicoes(List<SelectItem> posicoes) {
		this.posicoes = posicoes;
	}
}
