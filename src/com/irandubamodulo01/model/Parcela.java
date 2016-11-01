package com.irandubamodulo01.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by henrique on 03/05/2015.
 */
@Entity
@Table(name = "parcela")
public class Parcela implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column
    private Date dataPagamento;

    @Column
    private BigDecimal valor;

    @Column
    private BigDecimal porcentagemComissao;

    @Column
    private BigDecimal valorComissao;

    @Column
    private Integer numeroParcela;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getPorcentagemComissao() {
        return porcentagemComissao;
    }

    public void setPorcentagemComissao(BigDecimal porcentagemComissao) {
        this.porcentagemComissao = porcentagemComissao;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public BigDecimal getValorComissao() {
        valorComissao = BigDecimal.ZERO;
        if (valor != null && porcentagemComissao != null)
            valorComissao = valor.multiply(porcentagemComissao.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN, 2));
        return valorComissao;
    }

    public void setValorComissao(BigDecimal valorComissao) {
        this.valorComissao = valorComissao;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }


    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parcela parcela = (Parcela) o;

        if (id != null ? !id.equals(parcela.id) : parcela.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
