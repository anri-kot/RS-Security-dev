package com.rssecurity.storemanager.relatorio.dto;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;

import com.rssecurity.storemanager.compra.dto.CompraDTO;

public class RelatorioCompraDTO {
    private Page<CompraDTO> compras;
    private String interval;
    private BigDecimal custoTotal;

    private String erro = null;

    public RelatorioCompraDTO(){}

    public RelatorioCompraDTO(Page<CompraDTO> compras, String interval, String erro) {
        this.compras = compras;
        this.interval = interval;
        this.erro = erro;
    }

    public Page<CompraDTO> getCompras() {
        return compras;
    }

    public void setCompras(Page<CompraDTO> compras) {
        this.compras = compras;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public BigDecimal getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(BigDecimal custoTotal) {
        this.custoTotal = custoTotal;
    }
}
