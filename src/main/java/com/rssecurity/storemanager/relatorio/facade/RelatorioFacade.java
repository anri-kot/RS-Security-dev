package com.rssecurity.storemanager.relatorio.facade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.rssecurity.storemanager.compra.service.CompraService;
import com.rssecurity.storemanager.relatorio.dto.LucroVendaDTO;
import com.rssecurity.storemanager.relatorio.dto.RelatorioViewDTO;
import com.rssecurity.storemanager.relatorio.service.LucroVendaService;
import com.rssecurity.storemanager.util.FormatterUtil;
import com.rssecurity.storemanager.venda.dto.VendaDTO;
import com.rssecurity.storemanager.venda.service.VendaService;

@Component
public class RelatorioFacade {
    private final VendaService vendaService;
    private final CompraService compraService;
    private final LucroVendaService lucroVendaService;

    public RelatorioFacade(VendaService vendaService, CompraService compraService, LucroVendaService lucroVendaService) {
        this.vendaService = vendaService;
        this.compraService = compraService;
        this.lucroVendaService = lucroVendaService;
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

            List<Long> vendaIds = vendas.getContent().stream().map(VendaDTO::idVenda).toList();
            Map<Long, LucroVendaDTO> lucroVendas = lucroVendaService.findAllById(vendaIds).stream()
                    .collect(Collectors.toMap(LucroVendaDTO::idVenda, Function.identity()));
            BigDecimal lucroTotal = lucroVendaService.calculateLucroTotal(startDate, endDate);
            BigDecimal custoTotal = lucroVendaService.calculateCustoTotal(startDate, endDate);
    

            dto.setVendas(vendas);
            dto.setTotal(total);
            dto.setMonthlyTotal(monthlyTotal);
            dto.setTarget("compras");
            dto.setLucroVendas(lucroVendas);
            dto.setLucroTotal(lucroTotal);
            dto.setCustoTotal(custoTotal);

            try {
                dto.setInterval(FormatterUtil.formatInterval(startDate, endDate));
            } catch (Exception e) {
                throw new RuntimeException("Formato de data inválido: " + startDateString + " " + endDateString);
            }

            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Erro: " + e.getMessage());
        }
    }
}
