package com.rssecurity.storemanager.compra.model;

import java.math.BigDecimal;

import com.rssecurity.storemanager.produto.model.Produto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ItemCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItem;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    @ManyToOne
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;
    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;

    public Long getidItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
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
