package com.rssecurity.storemanager.relatorio.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssecurity.storemanager.relatorio.dto.RelatorioVendaDTO;
import com.rssecurity.storemanager.relatorio.dto.RelatorioViewDTO;
import com.rssecurity.storemanager.relatorio.facade.RelatorioFacade;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/relatorios")
public class RelatorioViewController {

    private final RelatorioFacade relatorioFacade;

    public RelatorioViewController(RelatorioFacade relatorioFacade) {
        this.relatorioFacade = relatorioFacade;
    }

    @GetMapping
    public String getPage(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "15") Integer size) {
        String todayString = LocalDate.now().toString();

        RelatorioViewDTO<RelatorioVendaDTO> view = relatorioFacade.getRelatorioView(todayString, todayString, currentPage, size);

        model.addAttribute("view", view);

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

        RelatorioViewDTO<RelatorioVendaDTO> view = relatorioFacade.getRelatorioView(
                dataInicio, 
                dataFim, 
                currentPage, 
                size);
        model.addAttribute("view", view);

        return Boolean.TRUE.equals(request.getAttribute("layoutDisabled"))
                ? "relatorios :: content"
                : "relatorios";
    }

    // TODO: implement compra view at relatorios

}
