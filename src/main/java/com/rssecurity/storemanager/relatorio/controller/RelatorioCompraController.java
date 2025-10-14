package com.rssecurity.storemanager.relatorio.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rssecurity.storemanager.compra.service.CompraService;
import com.rssecurity.storemanager.relatorio.dto.CustoCompraDTO;
import com.rssecurity.storemanager.relatorio.service.CustoCompraService;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioCompraController {
    private final CompraService compraService;
    private final CustoCompraService custoService;

    public RelatorioCompraController(CompraService compraService, CustoCompraService custoService) {
        this.compraService = compraService;
        this.custoService = custoService;
    }

    @PostMapping("custo_compra/list")
    public ResponseEntity<List<CustoCompraDTO>> findAllById(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(custoService.findAllById(ids));
    }

    @GetMapping("custo_compra/{id}")
    public ResponseEntity<CustoCompraDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(custoService.findById(id));
    }

    @GetMapping("custo_compra")
    public ResponseEntity<Map<String,BigDecimal>> calculateCustoTotalBetween(@RequestParam String dataInicio, @RequestParam String dataFim) {
        BigDecimal custoTotal = custoService.calculateCustoTotalBetween(LocalDate.parse(dataInicio), LocalDate.parse(dataFim));
        return ResponseEntity.ok(Map.of("custo_total", custoTotal));
    }

}
