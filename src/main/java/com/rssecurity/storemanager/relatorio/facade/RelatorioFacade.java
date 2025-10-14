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

import com.rssecurity.storemanager.compra.dto.CompraDTO;
import com.rssecurity.storemanager.compra.service.CompraService;
import com.rssecurity.storemanager.relatorio.dto.LucroVendaDTO;
import com.rssecurity.storemanager.relatorio.dto.RelatorioCompraDTO;
import com.rssecurity.storemanager.relatorio.dto.RelatorioVendaDTO;
import com.rssecurity.storemanager.relatorio.dto.RelatorioViewDTO;
import com.rssecurity.storemanager.relatorio.service.CustoCompraService;
import com.rssecurity.storemanager.relatorio.service.LucroVendaService;
import com.rssecurity.storemanager.util.FormatterUtil;
import com.rssecurity.storemanager.venda.dto.VendaDTO;
import com.rssecurity.storemanager.venda.service.VendaService;

@Component
public class RelatorioFacade {
    private final VendaService vendaService;
    private final CompraService compraService;
    private final LucroVendaService lucroService;
    private final CustoCompraService custoService;

    public RelatorioFacade(VendaService vendaService, CompraService compraService, LucroVendaService lucroVendaService, CustoCompraService custoCompraService) {
        this.vendaService = vendaService;
        this.compraService = compraService;
        this.lucroService = lucroVendaService;
        this.custoService = custoCompraService;
    }

    /**
     * @param startDateString is a LocalDate string;
     * @param endDateString is a LocalDate string;
    */
    public RelatorioVendaDTO getRelatorioVenda(String startDateString, String endDateString, int currentPage, int size) {
        RelatorioVendaDTO dto = new RelatorioVendaDTO();
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
            Map<Long, LucroVendaDTO> lucroVendas = lucroService.findAllById(vendaIds).stream()
                    .collect(Collectors.toMap(LucroVendaDTO::idVenda, Function.identity()));
            BigDecimal lucroTotal = lucroService.calculateLucroTotalBetween(startDate, endDate);
            BigDecimal custoTotal = lucroService.calculateCustoMedioTotalBetween(startDate, endDate);
    

            dto.setVendas(vendas);
            dto.setTotal(total);
            dto.setMonthlyTotal(monthlyTotal);
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
            dto.setErro("Erro: " + e.getMessage());
        }
        return dto;
    }

    public RelatorioCompraDTO getRelatorioCompra(String startDateString, String endDateString, int currentPage, int size) {
        RelatorioCompraDTO dto = new RelatorioCompraDTO();
        int page = currentPage - 1;

        try {

            LocalDate startDate; 
            LocalDate endDate;

            if (startDateString != null || endDateString != null) {
                startDate = LocalDate.parse(startDateString);
                endDate = LocalDate.parse(endDateString);
            } else {
                LocalDate today = LocalDate.now();
                startDate = LocalDate.of(today.getYear(), today.getMonth(), 1);
                endDate = startDate.plusMonths(1).minusDays(1);
            }
    
            Map<String, String> dateFilter = new HashMap<>();
            dateFilter.put("dataInicio", startDateString);
            dateFilter.put("dataFim", endDateString);

            Page<CompraDTO> compras = compraService.findAllByCustomMatcher(page, size, dateFilter);
            BigDecimal custoTotal = custoService.calculateCustoTotalBetween(startDate, endDate);

            dto.setCompras(compras);
            dto.setCustoTotal(custoTotal);

            try {
                dto.setInterval(FormatterUtil.formatInterval(startDate, endDate));
            } catch (Exception e) {
                throw new RuntimeException("Formato de data inválido: " + startDateString + " " + endDateString);
            }

            return dto;
        } catch (Exception e) {
            dto.setErro("Erro: " + e.getMessage());
        }
        return dto;
    }

    public RelatorioViewDTO<RelatorioVendaDTO> getRelatorioVendaView(String startDateString, String endDateString, int currentPage, int size) {
        RelatorioVendaDTO relatorio = getRelatorioVenda(startDateString, endDateString, currentPage, size);
        return new RelatorioViewDTO<RelatorioVendaDTO>(
            startDateString,
            endDateString,
            "vendas",
            currentPage,
            relatorio.getVendas().getTotalPages(),
            size,
            relatorio
        );
    }

    public RelatorioViewDTO<RelatorioCompraDTO> getRelatorioCompraView(String startDateString, String endDateString, int currentPage, int size) {
        RelatorioCompraDTO relatorio = getRelatorioCompra(startDateString, endDateString, currentPage, size);
        return new RelatorioViewDTO<RelatorioCompraDTO>(
            startDateString,
            endDateString,
            "compras",
            currentPage,
            relatorio.getCompras().getTotalPages(),
            size,
            relatorio
        );
    }
}
