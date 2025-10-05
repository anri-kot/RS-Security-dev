package com.rssecurity.storemanager.relatorio.facade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.compra.service.CompraService;
import com.rssecurity.storemanager.relatorio.dto.RelatorioViewDTO;
import com.rssecurity.storemanager.util.FormatterUtil;
import com.rssecurity.storemanager.venda.dto.VendaDTO;
import com.rssecurity.storemanager.venda.service.VendaService;

@Component
public class RelatorioFacade {
    private final VendaService vendaService;
    private final CompraService compraService;

    public RelatorioFacade(VendaService vendaService, CompraService compraService) {
        this.vendaService = vendaService;
        this.compraService = compraService;
    }

    /**
     * @param startDateString is a LocalDate string;
     * @param endDateString is a LocalDate string;
    */
    public RelatorioViewDTO buildModel(String startDateString, String endDateString, int currentPage, int size) {
        RelatorioViewDTO dto = new RelatorioViewDTO();
        int page = currentPage - 1;

        try {
            LocalDate now = LocalDate.now();
            LocalDate startDate = LocalDate.parse(startDateString);
            LocalDate endDate = LocalDate.parse(endDateString);
            LocalDate startMonth = now.minusDays(now.getDayOfMonth());
            LocalDate endMonth = startMonth.plusMonths(1).minusDays(1);
    
            Map<String, String> dateFilter = new HashMap<>();
            dateFilter.put("dataInicio", startDateString);
            dateFilter.put("dataFim", endDateString);
    
            Page<VendaDTO> vendas = vendaService.findAllByCustomMatcher(page, size, dateFilter);
            BigDecimal total = vendaService.calculateTotalVendaValueBetween(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
            BigDecimal monthlyTotal = compraService.calculateTotalCompraValueBetween(startMonth, endMonth);
    

            dto.setVendas(vendas);
            dto.setTotal(total);
            dto.setMonthlyTotal(monthlyTotal);
            dto.setTarget("compras");

            if (!startDateString.equals(endDateString)) {
                try {
                    dto.setInterval(FormatterUtil.formatInterval(startDateString, endDateString));
                } catch (Exception e) {
                    throw new RuntimeException("Formato de data inválido: " + startDateString + " " + endDateString);
                }
            }

            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Erro: " + e.getMessage());
        }
    }
}
