package com.irandubamodulo01.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.view.*;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.irandubamodulo01.dao.ConfiguracoesDAO;
import com.irandubamodulo01.dao.PeixeDAO;
import com.irandubamodulo01.model.Configuracoes;
import com.irandubamodulo01.model.Peixe;
import com.irandubamodulo01.util.Filtro;


@Named(value="configuracoesBean")
@ViewScoped
public class ConfiguracoesBean implements Serializable {

	private @Inject Configuracoes configuracoes;
	private @Inject ConfiguracoesDAO configuracoesDAO;

	public ConfiguracoesBean(){
				
	}
	
	public void carregarConfiguracoes(){
		List<Configuracoes> list = configuracoesDAO.getConfiguracoes();
		
		configuracoes = list != null && list.size() > 0 ? list.get(0) : null;
		
		if (configuracoes == null){
			configuracoes = new Configuracoes();
			configuracoes.setPesoCacapa(BigDecimal.ZERO);
			configuracoes.setValorMinimoDebitoAdiantamento(BigDecimal.ZERO);
		}
	}
	
	public void salvarConfiguracoes(){
		try {
			configuracoesDAO.save(configuracoes);
		    addMessage("Informação", "Configurações salvas com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("Erro", "Não foi possivel salvar as configurações.", FacesMessage.SEVERITY_ERROR);
		}
	}
	
	public void addMessage(String summary, String detail, Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public Configuracoes getConfiguracoes(){
		if (configuracoes == null)
			return new Configuracoes();
		else
			return configuracoes;
	}

	public void setConfiguracoes(Configuracoes configuracoes) {
		this.configuracoes = configuracoes;
	}
	
}
