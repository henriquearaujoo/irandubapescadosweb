package com.irandubamodulo01.model;

import com.irandubamodulo01.enumerated.StatusPedido;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by henrique on 28/04/2015.
 */
@Entity
@Table(name = "pedido")
public class Pedido implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column
    private Date data;

    @Column
    private Date dataEntrega;

    @Column
    private BigDecimal valor;

    @Column
    private String codigo;

    @Column
    private Integer qtdeParcelas;

    @Column
    private String notaFiscal;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @ManyToOne
    private Vendedor vendedor;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Transportadora transportadora;

    @Column
    private String placaCarro;

    @OneToMany(mappedBy="pedido", fetch=FetchType.LAZY)
    private List<Produto> produtos;

    @OneToMany(mappedBy="pedido", fetch=FetchType.LAZY)
    private List<Parcela> parcelas;

    @OneToMany(mappedBy =  "pedido", fetch = FetchType.EAGER)
    private List<HistoricoPedido> historico;

    public Pedido(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Integer getQtdeParcelas() {
        return qtdeParcelas;
    }

    public void setQtdeParcelas(Integer qtdeParcelas) {
        this.qtdeParcelas = qtdeParcelas;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public String getPlacaCarro() {
        return placaCarro;
    }

    public void setPlacaCarro(String placaCarro) {
        this.placaCarro = placaCarro;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public List<HistoricoPedido> getHistorico() {
        return historico;
    }

    public void setHistorico(List<HistoricoPedido> historico) {
        this.historico = historico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pedido pedido = (Pedido) o;

        if (id != null ? !id.equals(pedido.id) : pedido.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public BigDecimal getPesoTotalProdutos(){
        BigDecimal pesoTotal = BigDecimal.ZERO;
        if (produtos != null && produtos.size() > 0){
            for (Produto produto : produtos){
                pesoTotal = pesoTotal.add(produto.getPeso());
            }
        }
        return pesoTotal;
    }

    public BigDecimal getValorTotal(){
        BigDecimal valorTotal = BigDecimal.ZERO;
        if (produtos != null && produtos.size() > 0){
            for (Produto produto : produtos){
                valorTotal = valorTotal.add(produto.getValorVenda().multiply(produto.getPeso()));
            }
        }
        return valorTotal;
    }

}
