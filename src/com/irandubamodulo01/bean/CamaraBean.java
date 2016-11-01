package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.irandubamodulo01.dao.CompraDAO;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.irandubamodulo01.dao.CamaraDAO;
import com.irandubamodulo01.dao.PosicaoCamaraDAO;
import com.irandubamodulo01.model.Barco;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.model.PosicaoCamara;
import com.irandubamodulo01.util.Filtro;

@Named(value = "camarabean")
@ViewScoped
public class CamaraBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject CamaraDAO camaraDao;
	private @Inject PosicaoCamaraDAO posicaoDao;
	private @Inject Camara camara;

	private Filtro filtro =  new Filtro();
	private LazyDataModel<Camara> model;
	private PosicaoCamara posicao;

	public CamaraBean(){
		model = new LazyDataModel<Camara>() {
			private static final long serialVersionUID = 1L;
			@Override
			public List<Camara> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setPropriedadeOrdenacao(sortField);
				setRowCount(camaraDao.quantidadeFiltrados(filtro));
				return camaraDao.filtrados(filtro);
			}
		};
		
	}

	public void salvar(){
	    try{
			camara =	camaraDao.save(camara);
			addMessage("Informação", "Câmara salva com sucesso.", FacesMessage.SEVERITY_INFO);
	    }catch(Exception e){
			e.printStackTrace();
	    	addMessage("Erro", "Não foi possivel salvar a câmara.", FacesMessage.SEVERITY_ERROR);
	    }
	} 
	
	public void delete(){
		try{
			camaraDao.remove(camara);
			addMessage("Informação", "Câmara excluída com sucesso.", FacesMessage.SEVERITY_INFO);
		}catch (Exception e){
			e.printStackTrace();
			addMessage("Erro", "Não foi possivel excluir a câmara.", FacesMessage.SEVERITY_ERROR);
		}

	}
	
	public void salvarPosicao(){
		posicao.setCamara(camara);
		posicaoDao.save(posicao);
		carregarPosicoes();
		addMessage("Salvo com sucesso", "Posição salva com sucesso", FacesMessage.SEVERITY_INFO);
	}
	
	public void deletePosicao(){
		posicaoDao.remove(posicao);
		carregarPosicoes();
	}
	
	public void carregarPosicoes(){
		camara.setPosicoes(new ArrayList<PosicaoCamara>());
		camara.setPosicoes(posicaoDao.getPosicoesByCamara(camara));
	}
	
	public String pageCadastro(){
		return  "camarascad?faces-redirect=true";
	}
	
	public String editar(){
		return "camarascad?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public String cancelar(){
		return "camaras?faces-redirect=true";
	}

	public void prepararCadastroPosicao(){
	   posicao = new PosicaoCamara();
	}
	
	public void addMessage(String summary, String detail, Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	//Getters and Setters
	public Camara getCamara() {
		return camara;
	}

	public void setCamara(Camara camara) {
		this.camara = camara;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public LazyDataModel<Camara> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Camara> model) {
		this.model = model;
	}

	public PosicaoCamara getPosicao() {
		return posicao;
	}

	public void setPosicao(PosicaoCamara posicao) {
		this.posicao = posicao;
	}

}
