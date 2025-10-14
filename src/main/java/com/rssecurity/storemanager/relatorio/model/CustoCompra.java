package com.rssecurity.storemanager.relatorio.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vw_custo_compra")
public class CustoCompra {
    @Id
    private Long idCompra;
    private LocalDateTime data;
    private BigDecimal custoTotal;

    public CustoCompra(){}

    public CustoCompra(Long idCompra, LocalDateTime data, BigDecimal custoTotal) {
        this.idCompra = idCompra;
        this.data = data;
        this.custoTotal = custoTotal;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idProduto) {
        this.idCompra = idProduto;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public BigDecimal getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(BigDecimal custoTotal) {
        this.custoTotal = custoTotal;
    }
}
