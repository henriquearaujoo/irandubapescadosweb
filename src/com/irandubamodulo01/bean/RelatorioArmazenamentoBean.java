package com.irandubamodulo01.bean;

import com.irandubamodulo01.dao.CamaraDAO;
import com.irandubamodulo01.dao.CompraDAO;
import com.irandubamodulo01.dao.PeixeDAO;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.Peixe;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.ReportUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrador on 20/04/2015.
 */

@Named(value = "relatorioArmazenamentoBean")
@ViewScoped
public class RelatorioArmazenamentoBean implements Serializable{

    private Filtro filtro = new Filtro();
    private Long idPeixe;
    private List<Peixe> peixes;
    private @Inject PeixeDAO peixeDAO;
    private @Inject
    CamaraDAO camaraDAO;
    private String filtroPeriodo;
    private String filtroPeixe;
    private String filtroRetirada;
    private StringBuilder filtroConsulta;
    private String periodo;
    private String nomePeixe;
    private List<SelectItem> listaCamaras;
    @Inject
    private Camara camara;

    public RelatorioArmazenamentoBean(){

    }


    public void carregarPeixes(){
        setPeixes(peixeDAO.getPeixesComplete());

        carregarCamaras();
    }

    public void imprimir(){
        periodo = "Não informado";
        filtroConsulta = new StringBuilder();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String path = request.getSession().getServletContext().getRealPath( "/" );

        Map parametros = new HashMap();
        verificaParamentros();

        parametros.put("periodo", periodo);
        parametros.put("filtro_armazenamento", filtroPeriodo);
        parametros.put("filtro_retirada",  filtroRetirada);
        parametros.put("filtro",  filtroPeixe);


        try {
            JasperDesign jd = null;
            jd = JRXmlLoader.load(path + "relatorios/relatorio_armazenamento.jrxml");
            JasperReport report = JasperCompileManager.compileReport(jd);
            ReportUtil.openReportCon("Relatório", "relatorio_armazenamento" + new SimpleDateFormat("HHmmssddMMyyyy").format(new Date()), report, parametros, camaraDAO.getConnection());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void limparFiltro(){

        filtro.setDataInicio(null);
        filtro.setDataFinal(null);
        nomePeixe = null;
    }

    public void verificaParamentros(){

        List<String> condicoes = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (filtro.getDataInicio() != null && filtro.getDataFinal() != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(filtro.getDataFinal());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            periodo = sdf.format(filtro.getDataInicio()) + " até " + sdf.format(filtro.getDataFinal());
            filtroPeriodo = " and arm.data between to_timestamp('" + sdf.format(filtro.getDataInicio()) + "', 'dd/MM/yyyy') AND to_timestamp('" + sdf.format(calendar.getTime()) + "', 'dd/MM/yyyy')";

        }else if (filtro.getDataInicio() != null && filtro.getDataFinal() == null){
            periodo = "Dia " + sdf.format(filtro.getDataInicio());
            filtroPeriodo = " and to_char(arm.data, 'dd/MM/yyyy') = '" + sdf.format(filtro.getDataInicio()) + "'";

        }else if (filtro.getDataInicio() == null && filtro.getDataFinal() != null){
            periodo = "Dia " + sdf.format(filtro.getDataInicio());
            filtroPeriodo = " and to_char(arm.data, 'dd/MM/yyyy') = '" + sdf.format(filtro.getDataFinal()) + "'";

        }else{
            filtroPeriodo =  "and arm.id != 0";
        }

        if (nomePeixe != null && !nomePeixe.isEmpty()) {
            Peixe peixe = peixeDAO.getPeixePorDescricao(nomePeixe);

            if (peixe != null) {
                filtroPeixe = "and p.id = " + peixe.getId();
            } else {
                filtroPeixe = "and p.id != 0";
            }
        }else
            filtroPeixe = "and p.id != 0";

        if (camara != null){
            filtroPeriodo += " and arm.camara_id = " + camara.getId();
            filtroRetirada = " and ret.camara_id = " + camara.getId();
            filtroPeixe += " and arm1.camara_id = " + camara.getId();
        }else
            filtroRetirada = " and ret.id != 0";

    }

    public List<String> completePeixe(String query){
        List<String> results = new ArrayList<String>();
        for (Peixe peixe : getPeixes()){
            if (peixe.getDescricao().toLowerCase().startsWith(query.toLowerCase()))
                results.add(peixe.getDescricao());
        }
        return results;
    }

    public void carregarCamaras(){
        setListaCamaras(new ArrayList<SelectItem>());

        List<Camara> camaras = camaraDAO.getAll(Camara.class);

        for (Camara camara : camaras){
            getListaCamaras().add(new SelectItem(camara, camara.getDescricao()));
        }
    }

    public List<Peixe> getPeixes() {
        return peixes;
    }

    public void setPeixes(List<Peixe> peixes) {
        this.peixes = peixes;
    }

    public Filtro getFiltro() {
        return filtro;
    }

    public void setFiltro(Filtro filtro) {
        this.filtro = filtro;
    }

    public Long getIdPeixe() {
        return idPeixe;
    }

    public void setIdPeixe(Long idPeixe) {
        this.idPeixe = idPeixe;
    }

    public String getNomePeixe() {
        return nomePeixe;
    }

    public void setNomePeixe(String nomePeixe) {
        this.nomePeixe = nomePeixe;
    }

    public List<SelectItem> getListaCamaras() {
        return listaCamaras;
    }

    public void setListaCamaras(List<SelectItem> listaCamaras) {
        this.listaCamaras = listaCamaras;
    }

    public Camara getCamara() {
        return camara;
    }

    public void setCamara(Camara camara) {
        this.camara = camara;
    }
}
