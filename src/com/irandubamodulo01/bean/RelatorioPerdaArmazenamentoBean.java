package com.irandubamodulo01.bean;

import com.irandubamodulo01.dao.CompraDAO;
import com.irandubamodulo01.dao.PeixeDAO;
import com.irandubamodulo01.model.Peixe;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.ReportUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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

@Named(value = "percaBean")
@ViewScoped
public class RelatorioPerdaArmazenamentoBean implements Serializable{

    private Filtro filtro = new Filtro();
    private Long idPeixe;
    private List<Peixe> peixes;
    private @Inject PeixeDAO peixeDAO;
    private @Inject CompraDAO compraDAO;
    private String filtroPeriodo;
    private String filtroPeixe;
    private StringBuilder filtroConsulta;
    private String periodo;

    public RelatorioPerdaArmazenamentoBean(){

    }


    public void carregarPeixes(){
        peixes = new ArrayList<Peixe>();
        peixes = peixeDAO.getAll(Peixe.class);
    }

    public void imprimir(){
        periodo = "Não informado";
        filtroConsulta = new StringBuilder();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String path = request.getSession().getServletContext().getRealPath( "/" );

        Map parametros = new HashMap();
        String groupBy = " group by c.id, p.id, c.descricao, p.descricao order by c.id";
        verificaParamentros();
        filtroConsulta.append(groupBy);

        parametros.put("periodo", periodo);
        parametros.put("filtro", filtroConsulta.toString());

        try {
            JasperDesign jd = null;
            jd = JRXmlLoader.load(path + "relatorios/relatorio_perda_armazenamento.jrxml");
            JasperReport report = JasperCompileManager.compileReport(jd);
            ReportUtil.openReportCon("Relatório", "relatorio_perda_armazenamento" + new SimpleDateFormat("HHmmssddMMyyyy").format(new Date()), report, parametros, compraDAO.getConnection(), "PDF");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void verificaParamentros(){

        List<String> condicoes = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (filtro.getDataInicio() != null && filtro.getDataFinal() != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(filtro.getDataFinal());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            periodo = sdf.format(filtro.getDataInicio()) + " até " + sdf.format(filtro.getDataFinal());
            filtroPeriodo = "  arm.data between to_timestamp('" + sdf.format(filtro.getDataInicio()) + "', 'dd/MM/yyyy') AND to_timestamp('" + sdf.format(calendar.getTime()) + "', 'dd/MM/yyyy')";
            condicoes.add(filtroPeriodo);
        }else if (filtro.getDataInicio() != null && filtro.getDataFinal() == null){
            periodo = "Dia " + sdf.format(filtro.getDataInicio());
            filtroPeriodo = "  to_char(arm.data, 'dd/MM/yyyy') = '" + sdf.format(filtro.getDataInicio()) + "'";
            condicoes.add(filtroPeriodo);
        }else if (filtro.getDataInicio() == null && filtro.getDataFinal() != null){
            periodo = "Dia " + sdf.format(filtro.getDataInicio());
            filtroPeriodo = "  to_char(arm.data, 'dd/MM/yyyy') = '" + sdf.format(filtro.getDataFinal()) + "'";
            condicoes.add(filtroPeriodo);
        }

        if (idPeixe != null && idPeixe != 0){
            filtroPeixe =  "p.id = "+idPeixe;
            condicoes.add(filtroPeixe);
        }


        if (condicoes.size() > 0){
            for (int i = 0; i < condicoes.size(); i++) {
                filtroConsulta.append(" AND ");
                filtroConsulta.append(condicoes.get(i));
            }

        }

    }

    public void limparFiltro(){

        filtro.setDataInicio(null);
        filtro.setDataFinal(null);
    }

    public List<Peixe> getPeixes() {
        if (peixeDAO == null) {
            peixes = new ArrayList<Peixe>();
            peixes = peixeDAO.getAll(Peixe.class);
        }
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
}
