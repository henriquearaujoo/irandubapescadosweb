package com.irandubamodulo01.model;

import com.irandubamodulo01.util.FormatterUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by henrique on 28/04/2015.
 */
@Entity
@Table(name = "produto")
public class Produto implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column
    private BigDecimal peso;

    @Column
    private BigDecimal qtdEmEstoque;

    @Column
    private BigDecimal valorVenda;

    @ManyToOne
    @JoinColumn(name = "peixe_id")
    private Peixe peixe;

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

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Transient
    private List<Romaneio> romaneios;

    @Transient
    public String getDescricao(){
        return peso.toString() + " kg de " + peixe.getDescricao() + ", " + tamanhoPeixe.getDescricao() +", " + tipoPeixe.getDescricao() + ", de R$ " + FormatterUtil.getValorFormatado(peixe.getValor()) + " por R$ " + FormatterUtil.getValorFormatado(valorVenda);
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public BigDecimal getQtdEmEstoque() {
        return qtdEmEstoque;
    }

    public void setQtdEmEstoque(BigDecimal qtdEmEstoque) {
        this.qtdEmEstoque = qtdEmEstoque;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public Peixe getPeixe() {
        return peixe;
    }

    public void setPeixe(Peixe peixe) {
        this.peixe = peixe;
    }

    public TipoPeixe getTipoPeixe() {
        return tipoPeixe;
    }

    public void setTipoPeixe(TipoPeixe tipoPeixe) {
        this.tipoPeixe = tipoPeixe;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<Romaneio> getRomaneios() {
        return romaneios;
    }

    public void setRomaneios(List<Romaneio> romaneios) {
        this.romaneios = romaneios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Produto produto = (Produto) o;

        if (id != null ? !id.equals(produto.id) : produto.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
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

    public TamanhoPeixe getTamanhoPeixe() {
        return tamanhoPeixe;
    }

    public void setTamanhoPeixe(TamanhoPeixe tamanhoPeixe) {
        this.tamanhoPeixe = tamanhoPeixe;
    }
}
