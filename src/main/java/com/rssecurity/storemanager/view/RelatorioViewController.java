package com.rssecurity.storemanager.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.service.VendaService;
import com.rssecurity.storemanager.util.DateTimeUtil;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/relatorios")
public class RelatorioViewController {

    private final VendaService vendaService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");

    public RelatorioViewController(VendaService vendaService) {
        this.vendaService = vendaService;
    }
    
    @GetMapping
    public String getPage(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "15") Integer size
        ) {
        String todayString = LocalDate.now().toString();

        model.addAllAttributes(buildModel(todayString, todayString, currentPage, size));
        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                ? "relatorios :: content"
                : "relatorios";
    }

    @GetMapping("/vendas")
    public String gerarRelatorioVendas(
        @RequestParam String start,
        @RequestParam String end,
        Model model,
        @RequestParam(name = "page", defaultValue = "1") int currentPage,
        @RequestParam(defaultValue = "15") Integer size
    ) {
        model.addAllAttributes(buildModel(start, end, currentPage, size));
        return "fragments/relatorio :: resultado";
    }

    private String formatInterval(String start, String end) {
        String str1 = LocalDate.parse(start).format(formatter).toString();
        String str2 = LocalDate.parse(end).format(formatter).toString();
        return str1 + " - " + str2;
    }

    /**
     * @param startDateString is a LocalDate string;
     * @param endDateString is a LocalDate string;
    */
    private Map<String, Object> buildModel(String startDateString, String endDateString, int currentPage, int size) {
        Map<String, Object> modelMap = new HashMap<>();
        int page = currentPage - 1;
        try {
            LocalDate startDate = LocalDate.parse(startDateString);
            LocalDate endDate = LocalDate.parse(endDateString);
    
            Map<String, String> dateFilter = new HashMap<>();
            dateFilter.put("dataInicio", startDateString);
            dateFilter.put("dataFim", endDateString);
    
            Page<VendaDTO> vendas = vendaService.findAllByCustomMatcher(page, size, dateFilter);
            BigDecimal total = vendaService.calculateTotalVendaValueBetween(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
    
            modelMap.put("vendas", vendas);
            modelMap.put("total", total);
            modelMap.put("currentPage", currentPage);
            modelMap.put("totalPages", vendas.getTotalPages());
            modelMap.put("target", "compras");

            if (!startDateString.equals(endDateString)) {
                try {
                    modelMap.put("interval", formatInterval(startDateString, endDateString));
                } catch (Exception e) {
                    modelMap.put("erro", "Formato de data inválido: " + startDateString + " " + endDateString);
                }
            }

            return modelMap;
        } catch (Exception e) {
            modelMap.put("erro", e.getMessage());
            return modelMap;
        }
    }
}
