package com.irandubamodulo01.model;

import com.irandubamodulo01.bean.UsuarioSessionBean;

import javax.faces.context.FacesContext;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Henrique on 29/08/2016.
 */
@Entity
public class HistoricoPedido implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition="TEXT")
    private String observacao;
    @Column
    private Date data;
    @ManyToOne
    private Pedido pedido;
    @ManyToOne
    private Usuario usuario;

    public HistoricoPedido(){
        this.observacao = "";
        this.pedido = new Pedido();
        this.usuario = new Usuario();
        UsuarioSessionBean usuarioSession = (UsuarioSessionBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuariologado");
        this.usuario = usuarioSession != null ? usuarioSession.getUsuario() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
