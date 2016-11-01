package com.irandubamodulo01.model;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by henrique on 2015-04-08.
 */
@Entity
@Table(name="ultimologin")
public class UltimoLogin implements Serializable{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date data;

    @OneToOne
    private Usuario usuario;

    @Transient
    private String dataFormatada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDataFormatada() {
        if (data != null)
            dataFormatada = new SimpleDateFormat("dd/MM/yyyy    HH:mm:ss").format(data);
        return dataFormatada;
    }

    public void setDataFormatada(String dataFormatada) {
        this.dataFormatada = dataFormatada;
    }
}
