package com.rssecurity.storemanager.relatorio.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssecurity.storemanager.compra.service.CompraService;
import com.rssecurity.storemanager.produto.service.ProdutoService;
import com.rssecurity.storemanager.relatorio.dto.LucroProdutoDTO;
import com.rssecurity.storemanager.relatorio.dto.RelatorioViewDTO;
import com.rssecurity.storemanager.relatorio.facade.RelatorioFacade;
import com.rssecurity.storemanager.venda.dto.ItemVendaDTO;
import com.rssecurity.storemanager.venda.dto.VendaDTO;
import com.rssecurity.storemanager.venda.service.VendaService;

import jakarta.servlet.http.HttpServletRequest;

/* TODO: Refactor this class into DTO, RelatorioService and Utils */

@Controller
@RequestMapping("/relatorios")
public class RelatorioViewController {

    private final ProdutoService produtoService;
    private final RelatorioFacade relatorioFacade;

    public RelatorioViewController(ProdutoService produtoService, RelatorioFacade relatorioFacade) {
        this.produtoService = produtoService;
        this.relatorioFacade = relatorioFacade;
    }

    @GetMapping
    public String getPage(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "15") Integer size) {
        String todayString = LocalDate.now().toString();
        RelatorioViewDTO relatorio = relatorioFacade.buildModel(todayString, todayString, currentPage, size);

        model.addAttribute("relatorio", relatorio);
        model.addAttribute("dataInicio", todayString);
        model.addAttribute("dataFim", todayString);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", relatorio.getVendas().getTotalPages());
        model.addAttribute("size", size);

        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                ? "relatorios :: content"
                : "relatorios";
    }

    @GetMapping("/vendas")
    public String gerarRelatorioVendas(
            HttpServletRequest request,
            Model model,
            @RequestParam String dataInicio,
            @RequestParam String dataFim,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "15") Integer size) {
        try {
            RelatorioViewDTO relatorio = relatorioFacade.buildModel(dataInicio, dataFim, currentPage, size);

            model.addAttribute("relatorio", relatorio);
            model.addAttribute("dataInicio", dataInicio);
            model.addAttribute("dataFim", dataFim);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPages", relatorio.getVendas().getTotalPages());
            model.addAttribute("size", size);
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
        }

        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                ? "relatorios :: content"
                : "relatorios";
    }

}
