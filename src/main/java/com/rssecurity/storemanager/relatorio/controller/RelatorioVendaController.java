package com.rssecurity.storemanager.relatorio.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rssecurity.storemanager.excel.service.FileDownloadService;
import com.rssecurity.storemanager.excel.writter.RelatorioLucroWritter;
import com.rssecurity.storemanager.relatorio.dto.LucroVendaDTO;
import com.rssecurity.storemanager.relatorio.service.LucroVendaService;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioVendaController {
    private final LucroVendaService lucroVendaService;
    private final RelatorioLucroWritter excelWritter;
    private final FileDownloadService downloadService;

    public RelatorioVendaController(LucroVendaService lucroVendaService, RelatorioLucroWritter excelWritter,
            FileDownloadService downloadService) {
        this.lucroVendaService = lucroVendaService;
        this.excelWritter = excelWritter;
        this.downloadService = downloadService;
    }

    @GetMapping("/receita")
    public ResponseEntity<BigDecimal> calculateTotalVendaValueBetween(@RequestParam String start,
            @RequestParam String end) {
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

    @GetMapping("/export")
    public ResponseEntity<Resource> export(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<LucroVendaDTO> lucros = lucroVendaService.findByDataBetween(dataInicio.atStartOfDay(), dataFim.atTime(LocalTime.MAX));
        Resource res = downloadService.workbookToResource(excelWritter.export(lucros));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_lucro.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(res);
    }
}
