package com.rssecurity.storemanager.relatorio.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import com.rssecurity.storemanager.util.DateTimeUtil;
import com.rssecurity.storemanager.venda.service.VendaService;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioController {

    private final VendaService vendaService;
    private final LucroVendaService lucroVendaService;

    public RelatorioController(VendaService vendaService, LucroVendaService lucroVendaService) {
        this.vendaService = vendaService;
        this.lucroVendaService = lucroVendaService;
    }

    @GetMapping("/receita")
    public ResponseEntity<BigDecimal> calculateTotalVendaValueBetween(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startDateTime = DateTimeUtil.parseStartOfDay(start);
        LocalDateTime endDateTime = DateTimeUtil.parseEndOfDay(end);

        return ResponseEntity.ok(vendaService.calculateTotalVendaValueBetween(startDateTime, endDateTime));
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
