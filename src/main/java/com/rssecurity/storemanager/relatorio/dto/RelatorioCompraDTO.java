package com.rssecurity.storemanager.relatorio.dto;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.rssecurity.storemanager.compra.dto.CompraDTO;

public class RelatorioCompraDTO {
    private Page<CompraDTO> compras;
    private String interval;
    private Map<Long, CustoCompraDTO> custoCompras;

    private String erro = null;

    public RelatorioCompraDTO(Page<CompraDTO> compras, String interval, Map<Long, CustoCompraDTO> custoCompras,
            String erro) {
        this.compras = compras;
        this.interval = interval;
        this.custoCompras = custoCompras;
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

    public Map<Long, CustoCompraDTO> getCustoCompras() {
        return custoCompras;
    }

    public void setCustoCompras(Map<Long, CustoCompraDTO> custoCompras) {
        this.custoCompras = custoCompras;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public BigDecimal getCustoTotal() {
        BigDecimal custoTotal = new BigDecimal(0);
        custoCompras.values().stream()
                .map(custo -> custoTotal.add(custo.custoTotal()));
        return custoTotal;
    }
}
