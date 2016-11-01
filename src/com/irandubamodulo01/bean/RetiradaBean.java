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

@Named(value = "retiradaBean")
@ViewScoped
public class RetiradaBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject RetiradaDAO retiradaDao;
	private @Inject Retirada retirada;
	private @Inject
	ObservacaoArmazenamentoDAO observacaoArmazenamentoDAO;
	private Filtro filtro =  new Filtro();
	private LazyDataModel<Retirada> model;
	private Usuario usuario;
    private List<SelectItem> listaStatus;
	private ObservacaoArmazenamento observacao = new ObservacaoArmazenamento();

	//Variaveis temporarias para cadastro de armazenamento
	private Boolean inclusaoContinua = true;
	private String nomePeixe;
	private List<Peixe> peixes;
	private @Inject PeixeDAO peixeDAO;
	private @Inject TipoPeixeDAO tipoPeixeDAO;
	private @Inject CamaraDAO camaraDAO;
	private @Inject PosicaoCamaraDAO posicaoCamaraDAO;
	private List<SelectItem> tiposPeixe;
	private List<SelectItem> camaras;
	private List<SelectItem> posicoes;

	public UsuarioSessionBean getUsuarioSession(){
		UsuarioSessionBean usuarioSession = (UsuarioSessionBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuariologado");
		return usuarioSession;
	}

	public RetiradaBean(){
		usuario = getUsuarioSession().getUsuario();
		carregarFiltroStatus();
		model = new LazyDataModel<Retirada>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<Retirada> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				filtro.setUsuario(usuario);
				setRowCount(retiradaDao.quantidadeFiltrados(filtro));
				return retiradaDao.filtrados(filtro);
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

	public String retornar(){
		try {

		 observacao.setData(new Date());
		 observacao.setUsuario(getUsuarioSession().getUsuario());
		 observacao.setRetirada(retirada);
		 retirada.setStatus(StatusArmazenamento.RETORNADO);
		 retiradaDao.save(retirada);
		 observacaoArmazenamentoDAO.save(observacao);
		 addMessage("Retornado", "Armazenamento retornado para corre��o", FacesMessage.SEVERITY_INFO);
			return "retirada?faces-redirect=true";
		} catch (Exception e) {
		 e.printStackTrace();	
	     addMessage("Erro", "Erro ao estornar entre em contato com suporte t�cnico", FacesMessage.SEVERITY_ERROR);
			return null;
		}
	}
	
	public String autorizar(){
		try {
		 observacao.setData(new Date());
		 observacao.setUsuario(getUsuarioSession().getUsuario());
		 observacao.setRetirada(retirada);
		 retirada.setStatus(StatusArmazenamento.AUTORIZADO);
		 retiradaDao.save(retirada);
		 observacaoArmazenamentoDAO.save(observacao);

		 addMessage("Autorizado", "Retirada em estoque", FacesMessage.SEVERITY_INFO);
		 return "retirada?faces-redirect=true";
		} catch (Exception e) {
	     addMessage("Erro", "Erro ao autorizar entre em contato com suporte t�cnico", FacesMessage.SEVERITY_INFO);
			return null;
		}
	}

	public String prepararTelaCad(){
		return  "retiradacad?faces-redirect=true";
	}

	public void inicializarRetirada(){
		setPeixes(peixeDAO.getPeixesCompleteVenda());

		carregarTipos();
		carregarCamaras();
		carregarPosicoes2();

		if (getRetirada().getId() == null) {
			getRetirada().setQtdeEmbalagem(1);
		}else{
			nomePeixe = getRetirada().getPeixe().getDescricao();
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

	public void carregarTipos(){
		List<TipoPeixe> tipoPeixes = tipoPeixeDAO.getTiposComplete();

		this.tiposPeixe = new ArrayList<SelectItem>();

		for (TipoPeixe p : tipoPeixes){
			this.tiposPeixe.add(new SelectItem(p, p.getDescricao()));
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
		if (retirada != null && retirada.getCamara() != null) {
			List<PosicaoCamara> posicoes = posicaoCamaraDAO.getPosicoesByCamara(retirada.getCamara());

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
			getRetirada().setPeixe(peixeDAO.getPeixePorDescricao(nomePeixe));
			getRetirada().setStatus(StatusArmazenamento.AUTORIZADO);
			getRetirada().setData(new Date());
			getRetirada().setUsuario(getUsuarioSession().getUsuario());
			setRetirada(retiradaDao.save(getRetirada()));
			addMessage("Informação", "Retirada salva com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch(Exception e){
			addMessage("Erro", "Não foi possível salvar a retirada.", FacesMessage.SEVERITY_ERROR);
		}

		if (getInclusaoContinua()) {
			retirada = new Retirada();
			retirada.setQtdeEmbalagem(1);
			nomePeixe = null;
			return "";
		}else
			return cancelar();
	}

	public String cancelar(){
		return "retirada?faces-redirect=true";
	}

	public String editar(){
		return "retiradacad?faces-redirect=true&amp;includeViewParams=true";
	}

	public void delete(){
		try {
			retiradaDao.remove(getRetirada());
			addMessage("Informação", "Retirada excluída com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possível excluir a retirada.", FacesMessage.SEVERITY_ERROR);
		}

	}
	
	public void addMessage(String summary, String detail, Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	public String redirecionarDetalhes(){
		return  "detalhes_retirada?faces-redirect=true&amp;includeViewParams=true";
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public Retirada getRetirada() {
		return retirada;
	}

	public void setRetirada(Retirada retirada) {
		this.retirada = retirada;
	}

	public LazyDataModel<Retirada> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Retirada> model) {
		this.model = model;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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

	public List<SelectItem> getPosicoes() {
		return posicoes;
	}

	public void setPosicoes(List<SelectItem> posicoes) {
		this.posicoes = posicoes;
	}

	public List<SelectItem> getCamaras() {
		return camaras;
	}

	public void setCamaras(List<SelectItem> camaras) {
		this.camaras = camaras;
	}

	public List<SelectItem> getTiposPeixe() {
		return tiposPeixe;
	}

	public void setTiposPeixe(List<SelectItem> tiposPeixe) {
		this.tiposPeixe = tiposPeixe;
	}

	public List<Peixe> getPeixes() {
		return peixes;
	}

	public void setPeixes(List<Peixe> peixes) {
		this.peixes = peixes;
	}

	public String getNomePeixe() {
		return nomePeixe;
	}

	public void setNomePeixe(String nomePeixe) {
		this.nomePeixe = nomePeixe;
	}

	public Boolean getInclusaoContinua() {
		return inclusaoContinua;
	}

	public void setInclusaoContinua(Boolean inclusaoContinua) {
		this.inclusaoContinua = inclusaoContinua;
	}
}
