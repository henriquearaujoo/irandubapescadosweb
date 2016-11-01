package com.irandubamodulo01.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by henrique on 06/05/2015.
 */
@Entity
@Table(name = "romaneio")
public class Romaneio {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column
    private String lote;

    @Column
    private Integer qtdeEmbalagens;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "tipopeixe_id")
    private TipoPeixe tipoPeixe;

    @ManyToOne
    @JoinColumn(name = "tamanhopeixe_id")
    private TamanhoPeixe tamanhoPeixe;

    @ManyToOne
    @JoinColumn(name = "camara_id")
    private Camara camara;

    @ManyToOne
    @JoinColumn(name = "posicaocamara_id")
    private PosicaoCamara posicaoCamara;

    @Column
    private BigDecimal peso;

    public Romaneio(){

    }

    public Romaneio(Long id, String lote, Integer qtdeEmbalagens){
        this.id = id;
        this.lote = lote;
        this.qtdeEmbalagens = qtdeEmbalagens;
    }

    public Romaneio(Long id, String lote, BigDecimal peso,  Integer qtdeEmbalagens, Long idTamanho, String descricaoTamanho, Long idCamara, String descricaoCamara, Long idPosicao, String descricaoPosicao){
        this.id = id;
        this.lote = lote;
        this.peso = peso;
        this.qtdeEmbalagens = qtdeEmbalagens;
        if (idTamanho != null)
        {
            this.tamanhoPeixe = new TamanhoPeixe();
            this.tamanhoPeixe.setId(idTamanho);
            this.tamanhoPeixe.setDescricao(descricaoTamanho);
        }
        this.camara = new Camara();
        this.camara.setId(idCamara);
        this.camara.setDescricao(descricaoCamara);

        this.posicaoCamara = new PosicaoCamara();
        this.posicaoCamara.setId(idPosicao);
        this.posicaoCamara.setDescricao(descricaoPosicao);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Integer getQtdeEmbalagens() {
        return qtdeEmbalagens;
    }

    public void setQtdeEmbalagens(Integer qtdeEmbalagens) {
        this.qtdeEmbalagens = qtdeEmbalagens;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Romaneio romaneio = (Romaneio) o;

        if (id != null ? !id.equals(romaneio.id) : romaneio.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
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

    public PosicaoCamara getPosicaoCamara() {
        return posicaoCamara;
    }

    public void setPosicaoCamara(PosicaoCamara posicaoCamara) {
        this.posicaoCamara = posicaoCamara;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public TamanhoPeixe getTamanhoPeixe() {
        return tamanhoPeixe;
    }

    public void setTamanhoPeixe(TamanhoPeixe tamanhoPeixe) {
        this.tamanhoPeixe = tamanhoPeixe;
    }
}
