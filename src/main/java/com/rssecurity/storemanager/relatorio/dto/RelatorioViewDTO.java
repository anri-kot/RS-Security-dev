package com.rssecurity.storemanager.relatorio.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Page;

import com.rssecurity.storemanager.venda.dto.VendaDTO;

public class RelatorioViewDTO {
    private Page<VendaDTO> vendas;
    private BigDecimal total;
    private BigDecimal monthlyTotal;
    private String target;
    private String interval;
    private Map<Long, LucroVendaDTO> lucroVendas;
    private BigDecimal lucroTotal;

    private String erro = null;

    @Override
    public String toString() {
        return "vendas: " + vendas.getTotalElements() +
                "total: " + total.toString() + 
                "monthlyTotal: " + monthlyTotal.toString() +
                "target: " + target +
                "interval: " + interval +
                "lucroVendas: " + lucroVendas.size() +
                "lucroTotal: " + lucroTotal +
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

    public Map<Long, LucroVendaDTO> getLucroVendas() {
        return lucroVendas;
    }

    public void setLucroVendas(Map<Long, LucroVendaDTO> lucroVendas) {
        this.lucroVendas = lucroVendas;
    }

    public BigDecimal getLucroTotal() {
        return lucroTotal;
    }

    public void setLucroTotal(BigDecimal lucroTotal) {
        this.lucroTotal = lucroTotal;
    }
    /*
     * modelMap.put("vendas", vendas);
     * modelMap.put("total", total);
     * modelMap.put("monthlyTotal", monthlyTotal);
     */

     
}
