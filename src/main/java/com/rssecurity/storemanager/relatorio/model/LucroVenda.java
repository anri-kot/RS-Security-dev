package com.rssecurity.storemanager.relatorio.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vw_lucro_venda")
public class LucroVenda {
    @Id
    private Long idVenda;
    private LocalDateTime data;
    private BigDecimal receitaTotal;
    private BigDecimal custoTotal;
    private BigDecimal lucroTotal;
    private BigDecimal lucroPercentual;

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
    public BigDecimal getReceitaTotal() {
        return receitaTotal;
    }
    public void setReceitaTotal(BigDecimal receitaTotal) {
        this.receitaTotal = receitaTotal;
    }
    public BigDecimal getCustoTotal() {
        return custoTotal;
    }
    public void setCustoTotal(BigDecimal custoTotal) {
        this.custoTotal = custoTotal;
    }
    public BigDecimal getLucroTotal() {
        return lucroTotal;
    }
    public void setLucroTotal(BigDecimal lucroTotal) {
        this.lucroTotal = lucroTotal;
    }
    public BigDecimal getLucroPercentual() {
        return lucroPercentual;
    }
    public void setLucroPercentual(BigDecimal lucroPercentual) {
        this.lucroPercentual = lucroPercentual;
    }
}
