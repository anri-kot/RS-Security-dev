package com.rssecurity.storemanager.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssecurity.storemanager.dto.ItemVendaDTO;
import com.rssecurity.storemanager.dto.LucroProdutoDTO;
import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.service.CompraService;
import com.rssecurity.storemanager.service.ProdutoService;
import com.rssecurity.storemanager.service.VendaService;

import jakarta.servlet.http.HttpServletRequest;

/* TODO: Refactor this class into DTO, RelatorioService and Utils */

@Controller
@RequestMapping("/relatorios")
public class RelatorioViewController {

    private final VendaService vendaService;
    private final CompraService compraService;
    private final ProdutoService produtoService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");

    public RelatorioViewController(VendaService vendaService, CompraService compraService, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.compraService = compraService;
        this.produtoService = produtoService;
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
        HttpServletRequest request,
        @RequestParam String start,
        @RequestParam String end,
        Model model,
        @RequestParam(name = "page", defaultValue = "1") int currentPage,
        @RequestParam(defaultValue = "15") Integer size
    ) {
        model.addAllAttributes(buildModel(start, end, currentPage, size));
        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                ? "relatorios :: content"
                : "relatorios";
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

            Map<Long, BigDecimal> lucroVenda = getLucroByVenda(vendas.getContent());
    
            modelMap.put("vendas", vendas);
            modelMap.put("start", startDateString);
            modelMap.put("end", endDateString);
            modelMap.put("total", total);
            modelMap.put("monthlyTotal", monthlyTotal);
            modelMap.put("currentPage", currentPage);
            modelMap.put("totalPages", vendas.getTotalPages());
            modelMap.put("target", "compras");
            modelMap.put("lucroVenda", lucroVenda);

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

    private Map<Long, BigDecimal> getLucroByVenda(List<VendaDTO> vendas) {
    Map<Long, BigDecimal> lucroVenda = new HashMap<>();
    List<Long> produtoIds = new ArrayList<>();

    for (VendaDTO venda : vendas) {
        for (ItemVendaDTO item : venda.itens()) {
            produtoIds.add(item.produto().idProduto());
        }
    }

    List<LucroProdutoDTO> lucros = produtoService.findAllLucroProdutoById(produtoIds);

    Map<Long, BigDecimal> lucroPorProduto = lucros.stream()
            .filter(dto -> dto.lucro() != null)
            .collect(Collectors.toMap(
                    LucroProdutoDTO::idProduto,
                    LucroProdutoDTO::lucro,
                    BigDecimal::add
            ));

    for (VendaDTO venda : vendas) {
        for (ItemVendaDTO item : venda.itens()) {
            Long idProduto = item.produto().idProduto();
            BigDecimal lucro = lucroPorProduto.getOrDefault(idProduto, BigDecimal.ZERO);

            lucroVenda.merge(venda.idVenda(), lucro, BigDecimal::add);
        }
    }

    return lucroVenda;
}

}
