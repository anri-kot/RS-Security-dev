package com.rssecurity.storemanager.relatorio.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rssecurity.storemanager.relatorio.dto.LucroVendaDTO;
import com.rssecurity.storemanager.relatorio.service.LucroVendaService;
import com.rssecurity.storemanager.venda.service.VendaService;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioVendaController {

    private final VendaService vendaService;
    private final LucroVendaService lucroVendaService;

    public RelatorioVendaController(VendaService vendaService, LucroVendaService lucroVendaService) {
        this.vendaService = vendaService;
        this.lucroVendaService = lucroVendaService;
    }

    @GetMapping("/receita")
    public ResponseEntity<BigDecimal> calculateTotalVendaValueBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDateTime = LocalDate.parse(start);
        LocalDate endDateTime = LocalDate.parse(end);

        return ResponseEntity.ok(lucroVendaService.calculateLucroTotalBetween(startDateTime, endDateTime));
    }

    @PostMapping("/lucro_venda/list")
    public ResponseEntity<List<LucroVendaDTO>> findAllById(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(lucroVendaService.findAllById(ids));
    }

    @GetMapping("/lucro_venda/{id}")
    public ResponseEntity<LucroVendaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(lucroVendaService.findById(id));
    }

}
