package com.irandubamodulo01.util;

import com.irandubamodulo01.model.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by henrique on 11/05/2015.
 */
public class EstoqueUtil {

    private Peixe peixe;

    private Camara camara;

    private PosicaoCamara posicaoCamara;

    private Date dataArmazenamento;

    private TipoPeixe tipo;

    private TamanhoPeixe tamanho;

    private BigDecimal peso;

    private BigDecimal pesoRetirada;

    private String descricaoCamaraPeixe;

    private String pesoTotal;

    public EstoqueUtil() {
    }

    public EstoqueUtil(Camara camara, String pesoTotal) {
        this.camara = camara;
        this.pesoTotal = pesoTotal;
    }

    public EstoqueUtil(PosicaoCamara posicaoCamara, String pesoTotal) {
        this.posicaoCamara = posicaoCamara;
        this.pesoTotal = pesoTotal;
    }

    public EstoqueUtil(Peixe peixe, String pesoTotal) {
        this.peixe = peixe;
        this.pesoTotal = pesoTotal;
    }

    public EstoqueUtil(TipoPeixe tipo, String pesoTotal) {
        this.tipo = tipo;
        this.pesoTotal = pesoTotal;
    }

    public EstoqueUtil(TamanhoPeixe tamanho, String pesoTotal) {
        this.tamanho = tamanho;
        this.pesoTotal = pesoTotal;
    }

    public EstoqueUtil(String descricaoCamaraPeixe) {
        this.descricaoCamaraPeixe = descricaoCamaraPeixe;
    }

    public EstoqueUtil(String descricaoCamaraPeixe, String pesoTotal) {
        this.descricaoCamaraPeixe = descricaoCamaraPeixe;
        this.pesoTotal = pesoTotal;
    }

    public EstoqueUtil(String descricaoCamaraPeixe, BigDecimal peso, BigDecimal pesoRetirada, String pesoTotal) {
        this.descricaoCamaraPeixe = descricaoCamaraPeixe;
        this.peso = peso;
        this.pesoRetirada = pesoRetirada;
        this.pesoTotal = pesoTotal;
    }

    public EstoqueUtil(Peixe peixe, Camara camara, PosicaoCamara posicaoCamara, Date dataArmazenamento, TipoPeixe tipo, BigDecimal peso, BigDecimal pesoRetirada) {
        this.peixe = peixe;
        this.camara = camara;
        this.posicaoCamara = posicaoCamara;
        this.dataArmazenamento = dataArmazenamento;
        this.tipo = tipo;
        this.peso = peso;
        this.pesoRetirada = pesoRetirada;
    }

    public EstoqueUtil(Long idPeixe, String peixe, Long idCamara, String camara, Long idPosicao, String posicao, Date dataArmazenamento, Long idTipo, String tipo, BigDecimal peso, BigDecimal pesoRetirada) {
        this.peixe = new Peixe();
        this.peixe.setId(idPeixe);
        this.peixe.setDescricao(peixe);
        this.camara = new Camara();
        this.camara.setId(idCamara);
        this.camara.setDescricao(camara);
        this.posicaoCamara = new PosicaoCamara();
        this.posicaoCamara.setId(idPosicao);
        this.posicaoCamara.setDescricao(posicao);
        this.dataArmazenamento = dataArmazenamento;
        this.tipo = new TipoPeixe();
        this.tipo.setId(idTipo);
        this.tipo.setDescricao(tipo);
        this.peso = peso;
        this.pesoRetirada = pesoRetirada;
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

    public PosicaoCamara getPosicaoCamara() {
        return posicaoCamara;
    }

    public void setPosicaoCamara(PosicaoCamara posicaoCamara) {
        this.posicaoCamara = posicaoCamara;
    }

    public Date getDataArmazenamento() {
        return dataArmazenamento;
    }

    public void setDataArmazenamento(Date dataArmazenamento) {
        this.dataArmazenamento = dataArmazenamento;
    }

    public TipoPeixe getTipo() {
        return tipo;
    }

    public void setTipo(TipoPeixe tipo) {
        this.tipo = tipo;
    }

    public TamanhoPeixe getTamanho() {
        return tamanho;
    }

    public void setTamanho(TamanhoPeixe tamanho) {
        this.tamanho = tamanho;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getPesoRetirada() {
        return pesoRetirada;
    }

    public void setPesoRetirada(BigDecimal pesoRetirada) {
        this.pesoRetirada = pesoRetirada;
    }

    public String getDescricao(){
        if (peixe != null && peso != null && pesoRetirada != null && dataArmazenamento != null && tipo != null && posicaoCamara != null) {
            BigDecimal peso = this.peso.subtract(pesoRetirada);
            return peso.toString() + " KG " + tipo.getDescricao();
        }else
            return "";
    }

    public BigDecimal getPesoLiquido(){
        if (peso != null && pesoRetirada != null){
            return peso.subtract(pesoRetirada);
        }else
            return null;
    }

    public String getDescricaoCamara(){
        if (camara != null)
            return camara.getDescricao() + " - " + pesoTotal + " kg";
        else
            return  null;
    }

    public String getDescricaoPosicao(){
        if (posicaoCamara != null)
            return posicaoCamara.getDescricao() + " - " + pesoTotal + " kg";
        else
            return null;
    }

    public String getDescricaoPeixe(){
        if (peixe != null)
            return peixe.getDescricao() + " - " + pesoTotal + " kg";
        else
            return null;
    }

    public String getDescricaoTipo(){
        if (tipo != null)
            return tipo.getDescricao() + " - " + pesoTotal + " kg";
        else
            return null;
    }

    public String getDescricaoTamanho(){
        if (tamanho != null)
            return tamanho.getDescricao() + " - " + pesoTotal + " kg";
        else
            return null;
    }

    public String getDescricaoCamaraPeixe() {
        return descricaoCamaraPeixe;
    }

    public void setDescricaoCamaraPeixe(String descricaoCamaraPeixe) {
        this.descricaoCamaraPeixe = descricaoCamaraPeixe;
    }

    public String getPesoTotal() {
        return pesoTotal;
    }

    public void setPesoTotal(String pesoTotal) {
        this.pesoTotal = pesoTotal;
    }
}
