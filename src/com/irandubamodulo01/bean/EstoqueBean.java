package com.irandubamodulo01.bean;

import com.irandubamodulo01.dao.*;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.Peixe;
import com.irandubamodulo01.model.TipoPeixe;
import com.irandubamodulo01.util.EstoqueUtil;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.FormatterUtil;
import com.irandubamodulo01.util.ReportUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrador on 20/04/2015.
 */

@Named(value = "estoqueBean")
@ViewScoped
public class EstoqueBean implements Serializable{

    private Filtro filtro = new Filtro();
    private Long idPeixe;
    private List<Peixe> peixes;
    private @Inject PeixeDAO peixeDAO;
    private @Inject ArmazenamentoDAO armazenamentoDAO;
    @Inject
    private TipoPeixeDAO tipoPeixeDAO;
    @Inject
    private CamaraDAO camaraDAO;
    private String nomePeixe;
    private Peixe peixe;
    private Camara camara;
    private TipoPeixe tipoPeixe;
    private TreeNode root;
    private List<SelectItem> listaTipos;
    private List<SelectItem> listaCamaras;

    public EstoqueBean(){

    }

    public void inicializarEstoque(){
        setPeixes(peixeDAO.getPeixesCompleteVenda());

        setNomePeixe(null);
        carregarTipos();
        carregarCamaras();

        root = new CheckboxTreeNode();
    }

    public void buscarEstoque(){

        if (nomePeixe != null && !nomePeixe.isEmpty()) {

            peixe = peixeDAO.getPeixePorDescricao(nomePeixe);

            List<EstoqueUtil> list = armazenamentoDAO.getEstoquePorPeixeCamaraTipo(peixe, camara, tipoPeixe);

            root = new CheckboxTreeNode(new EstoqueUtil());

            for (EstoqueUtil estoqueUtil : list) {

                TreeNode nodeCamara = camaraAdicionada(estoqueUtil.getCamara().getDescricao());
                if (nodeCamara == null) {
                    String totalCamara = FormatterUtil.getValorFormatado(list.stream().filter(c -> c.getCamara().getDescricao().equals(estoqueUtil.getCamara().getDescricao())).map(EstoqueUtil::getPesoLiquido).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
                    nodeCamara = new CheckboxTreeNode(new EstoqueUtil(estoqueUtil.getCamara(), totalCamara), root);
                    nodeCamara.setExpanded(true);
                    nodeCamara.setSelectable(false);
                }

                TreeNode nodePosicao = posicaoAdicionada(estoqueUtil.getPosicaoCamara().getDescricao(), nodeCamara);
                if (nodePosicao == null) {
                    String totalPosicao = FormatterUtil.getValorFormatado(list.stream().filter(c -> c.getCamara().getDescricao().equals(estoqueUtil.getCamara().getDescricao()) && c.getPosicaoCamara().getDescricao().equals(estoqueUtil.getPosicaoCamara().getDescricao())).map(EstoqueUtil::getPesoLiquido).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
                    nodePosicao = new CheckboxTreeNode(new EstoqueUtil(estoqueUtil.getPosicaoCamara(), totalPosicao), nodeCamara);
                    nodePosicao.setExpanded(true);
                    nodePosicao.setSelectable(false);
                }

                TreeNode nodePeixe = peixeAdicionado(estoqueUtil.getPeixe().getDescricao(), nodePosicao);
                if (nodePeixe == null) {
                    BigDecimal pesoPeixe = list.stream().filter(c -> c.getCamara().getDescricao().equals(estoqueUtil.getCamara().getDescricao()) && c.getPosicaoCamara().getDescricao().equals(estoqueUtil.getPosicaoCamara().getDescricao()) && c.getPeixe().getDescricao().equals(estoqueUtil.getPeixe().getDescricao()))
                            .map(EstoqueUtil::getPesoLiquido).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

                    if (pesoPeixe.compareTo(BigDecimal.ZERO) == 1) {
                        String totalPeixe = FormatterUtil.getValorFormatado(pesoPeixe);
                        EstoqueUtil ePeixe = new EstoqueUtil(estoqueUtil.getPeixe(), totalPeixe);
                        nodePeixe = new CheckboxTreeNode(ePeixe, nodePosicao);
                        nodePeixe.setExpanded(true);
                        nodePeixe.setSelectable(false);
                    }
                }

                TreeNode nodeTipo = tipoAdicionado(estoqueUtil.getTipo().getDescricao(), nodePeixe);
                if (nodeTipo == null){
                    BigDecimal pesoTipo = list.stream().filter(c -> c.getCamara().getDescricao().equals(estoqueUtil.getCamara().getDescricao()) && c.getPosicaoCamara().getDescricao().equals(estoqueUtil.getPosicaoCamara().getDescricao()) && c.getPeixe().getDescricao().equals(estoqueUtil.getPeixe().getDescricao()) && c.getTipo().getDescricao().equals(estoqueUtil.getTipo().getDescricao()))
                            .map(EstoqueUtil::getPesoLiquido).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

                    if (pesoTipo.compareTo(BigDecimal.ZERO) == 1){
                        String totalTipo = FormatterUtil.getValorFormatado(pesoTipo);
                        EstoqueUtil eTipo = new EstoqueUtil(estoqueUtil.getTipo(), totalTipo);
                        nodeTipo = new CheckboxTreeNode(eTipo, nodePeixe);
                        nodeTipo.setExpanded(true);
                        nodeTipo.setSelectable(false);
                    }
                }

                TreeNode nodeTamanho = tamanhoAdicionado(estoqueUtil.getTamanho().getDescricao(), nodeTipo);
                if (nodeTamanho == null){
                    BigDecimal pesoTamanho = list.stream().filter(c -> c.getCamara().getDescricao().equals(estoqueUtil.getCamara().getDescricao()) && c.getPosicaoCamara().getDescricao().equals(estoqueUtil.getPosicaoCamara().getDescricao()) && c.getPeixe().getDescricao().equals(estoqueUtil.getPeixe().getDescricao()) && c.getTipo().getDescricao().equals(estoqueUtil.getTipo().getDescricao()) && c.getTamanho().getDescricao().equals(estoqueUtil.getTamanho().getDescricao()))
                            .map(EstoqueUtil::getPesoLiquido).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

                    if (pesoTamanho.compareTo(BigDecimal.ZERO) == 1){
                        String totalTamanho = FormatterUtil.getValorFormatado(pesoTamanho);
                        EstoqueUtil eTamanho = new EstoqueUtil(estoqueUtil.getTamanho(), totalTamanho);
                        nodeTamanho = new CheckboxTreeNode(eTamanho, nodeTipo);
                        nodeTamanho.setExpanded(true);
                        nodeTamanho.setSelectable(false);
                    }
                }

                /*EstoqueUtil eTipo = new EstoqueUtil(estoqueUtil.getPeixe(), estoqueUtil.getCamara(), estoqueUtil.getPosicaoCamara(), estoqueUtil.getDataArmazenamento(), estoqueUtil.getTipo(), estoqueUtil.getPeso(), estoqueUtil.getPesoRetirada());

                if (eTipo.getPesoLiquido().compareTo(BigDecimal.ZERO) == 1) {
                    TreeNode ntipo = new CheckboxTreeNode(eTipo, nodeTipo);
                    ntipo.setExpanded(true);
                    ntipo.setSelectable(false);
                }*/

            }
        }else
            addMessage("Aviso", "Selecione um peixe antes de efetuar a busca no estoque.", FacesMessage.SEVERITY_WARN);
    }

    public void addMessage(String summary, String detail, FacesMessage.Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public TreeNode camaraAdicionada(String camara){
        for (TreeNode node : root.getChildren()){
            if (((EstoqueUtil) node.getData()).getCamara().getDescricao().equals(camara))
                return node;
        }

        return null;
    }

    public TreeNode peixeAdicionado(String peixe, TreeNode nodePosicao){

        for (TreeNode node : nodePosicao.getChildren()){
            if (((EstoqueUtil) node.getData()).getPeixe().getDescricao().equals(peixe))
                return node;
        }

        return null;
    }

    public TreeNode posicaoAdicionada(String posicao, TreeNode nodeCamara){
        for (TreeNode node : nodeCamara.getChildren()){
            if (((EstoqueUtil) node.getData()).getPosicaoCamara().getDescricao().equals(posicao))
                return node;
        }

        return null;
    }

    public TreeNode tipoAdicionado(String tipo, TreeNode nodePeixe){
        for (TreeNode node : nodePeixe.getChildren()){
            if (((EstoqueUtil) node.getData()).getTipo().getDescricao().equals(tipo))
                return node;
        }

        return null;
    }

    public TreeNode tamanhoAdicionado(String tipo, TreeNode nodeTipo){
        for (TreeNode node : nodeTipo.getChildren()){
            if (((EstoqueUtil) node.getData()).getTamanho().getDescricao().equals(tipo))
                return node;
        }

        return null;
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
        setListaTipos(new ArrayList<SelectItem>());

        List<TipoPeixe> tipos = tipoPeixeDAO.getAll(TipoPeixe.class);

        for (TipoPeixe tipoPeixe : tipos){
            getListaTipos().add(new SelectItem(tipoPeixe, tipoPeixe.getDescricao()));
        }
    }

    public void carregarCamaras(){
        setListaCamaras(new ArrayList<SelectItem>());

        List<Camara> camaras = camaraDAO.getAll(Camara.class);

        for (Camara camara : camaras){
            getListaCamaras().add(new SelectItem(camara, camara.getDescricao()));
        }
    }

    public void imprimir(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String path = request.getSession().getServletContext().getRealPath( "/" );

        Map parametros = new HashMap();
        verificaParamentros();

        /*parametros.put("periodo", periodo);
        parametros.put("filtro_armazenamento", filtroPeriodo);
        parametros.put("filtro",  filtroPeixe);*/

        try {
            JasperDesign jd = null;
            jd = JRXmlLoader.load(path + "relatorios/relatorio_armazenamento.jrxml");
            JasperReport report = JasperCompileManager.compileReport(jd);
            ReportUtil.openReportCon("Relatório", "relatorio_armazenamento" + new SimpleDateFormat("HHmmssddMMyyyy").format(new Date()), report, parametros, armazenamentoDAO.getConnection());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void limparFiltro(){

        filtro.setDataInicio(null);
        filtro.setDataFinal(null);
    }

    public void verificaParamentros(){

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

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TipoPeixe getTipoPeixe() {
        return tipoPeixe;
    }

    public void setTipoPeixe(TipoPeixe tipoPeixe) {
        this.tipoPeixe = tipoPeixe;
    }

    public Camara getCamara() {
        return camara;
    }

    public void setCamara(Camara camara) {
        this.camara = camara;
    }

    public Peixe getPeixe() {
        return peixe;
    }

    public void setPeixe(Peixe peixe) {
        this.peixe = peixe;
    }

    public String getNomePeixe() {
        return nomePeixe;
    }

    public void setNomePeixe(String nomePeixe) {
        this.nomePeixe = nomePeixe;
    }

    public List<SelectItem> getListaTipos() {
        return listaTipos;
    }

    public void setListaTipos(List<SelectItem> listaTipos) {
        this.listaTipos = listaTipos;
    }

    public List<SelectItem> getListaCamaras() {
        return listaCamaras;
    }

    public void setListaCamaras(List<SelectItem> listaCamaras) {
        this.listaCamaras = listaCamaras;
    }
}
