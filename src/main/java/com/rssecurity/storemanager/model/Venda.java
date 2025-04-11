package com.rssecurity.storemanager.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenda;
    private LocalDateTime data;
    private String observacao;
    @ManyToOne
    @JoinColumn(name = "_id_usuario")
    private Usuario usuario;

    public Venda(Long idVenda, LocalDateTime data, String observacao) {
        this.idVenda = idVenda;
        this.data = data;
        this.observacao = observacao;
    }

    public Long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Long idVenda) {
        this.idVenda = idVenda;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Usuario getUsuario() {
        return  usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
