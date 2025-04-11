package com.rssecurity.storemanager.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Compra {
    @Id
    private Long idCompra;
    private LocalDateTime data;
    private String observacao;
    @ManyToOne
    @JoinColumn(name = "_id_fornecedor")
    private Fornecedor fornecedor;

    public Compra(Long idCompra, LocalDateTime data, String observacao, Fornecedor fornecedor) {
        this.idCompra = idCompra;
        this.data = data;
        this.observacao = observacao;
        this.fornecedor = fornecedor;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
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

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
}
