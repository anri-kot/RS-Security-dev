package com.rssecurity.storemanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

@Entity
public class ItemCompra {
    @Id
    private Long idCompra;
    private int quantidade;
    private BigDecimal valorUnitario;
    @ManyToOne
    @JoinColumn(name = "_id_compra")
    private Compra compra;
    @ManyToOne
    @JoinColumn(name = "_id_produto")
    private Produto produto;

    public ItemCompra(Long idCompra, int quantidade, BigDecimal valorUnitario, Compra compra, Produto produto) {
        this.idCompra = idCompra;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.compra = compra;
        this.produto = produto;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
