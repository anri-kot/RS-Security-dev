package com.rssecurity.storemanager.relatorio.dto;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.data.domain.Page;

import com.rssecurity.storemanager.venda.dto.VendaDTO;

public class RelatorioViewDTO {
    private Page<VendaDTO> vendas;
    private BigDecimal total;
    private BigDecimal monthlyTotal;
    private String target;
    private String interval;

    private String erro = null;

    @Override
    public String toString() {
        return "vendas: " + vendas.getTotalElements() +
                "total: " + total.toString() + 
                "monthlyTotal: " + monthlyTotal.toString() +
                "target: " + target +
                "interval: " + interval +
                "erro: " + Objects.requireNonNullElse(erro, "none");
    }

    public Page<VendaDTO> getVendas() {
        return vendas;
    }

    public void setVendas(Page<VendaDTO> vendas) {
        this.vendas = vendas;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getMonthlyTotal() {
        return monthlyTotal;
    }

    public void setMonthlyTotal(BigDecimal monthlyTotal) {
        this.monthlyTotal = monthlyTotal;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    /*
     * modelMap.put("vendas", vendas);
     * modelMap.put("total", total);
     * modelMap.put("monthlyTotal", monthlyTotal);
     */

     
}
