package com.rssecurity.storemanager.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rssecurity.storemanager.service.VendaService;
import com.rssecurity.storemanager.util.DateTimeUtil;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioController {
    @Autowired
    private VendaService vendaService;

    @GetMapping("/receita")
    public ResponseEntity<BigDecimal> calculateTotalVendaValueBetween(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startDateTime = DateTimeUtil.parseStartOfDay(start);
        LocalDateTime endDateTime = DateTimeUtil.parseEndOfDay(end);

        return ResponseEntity.ok(vendaService.calculateTotalVendaValueBetween(startDateTime, endDateTime));
    }
}
