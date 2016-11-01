package com.irandubamodulo01.bean;

import com.irandubamodulo01.util.Filtro;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrador on 15/04/2015.
 */
@Named(value = "percaProccesBean")
@ViewScoped
public class PercaProcessoBean implements Serializable{

    private Filtro filtro = new Filtro();

    public PercaProcessoBean(){}

    public void gerarRelatorio(){

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String path = request.getSession().getServletContext().getRealPath( "/" );

        Map parametros = new HashMap();
    }
}
