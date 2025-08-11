package com.rssecurity.storemanager.controller;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rssecurity.storemanager.dto.CompraDTO;
import com.rssecurity.storemanager.excel.writter.CompraExcelWritter;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.service.CompraService;
import com.rssecurity.storemanager.service.FileDownloadService;

@RestController
@RequestMapping("/api/compra")
public class CompraController {
    @Autowired
    private CompraService service;
    @Autowired
    private FileDownloadService downloadService;
    @Autowired
    private CompraExcelWritter excelWritter;

    // SEARCH
    
    @GetMapping
    public ResponseEntity<List<CompraDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{idCompra}")
    public ResponseEntity<CompraDTO> findById(@PathVariable Long idCompra) {
        return ResponseEntity.ok(service.findById(idCompra));
    }
    
    @GetMapping("/search/data")
    public ResponseEntity<List<CompraDTO>> findByData(@RequestParam LocalDate data) {
        return ResponseEntity.ok(service.findByData(data));
    }
    
    @GetMapping("/search/data-between")
    public ResponseEntity<List<CompraDTO>> findByDataBetween(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(service.findByDataBetween(inicio, fim));
    }

    @GetMapping("/search/data-after")
    public ResponseEntity<List<CompraDTO>> findByDataAfter(@RequestParam LocalDateTime data) {
        return ResponseEntity.ok(service.findByDataAfter(data));
    }

    @GetMapping("/search/data-before")
    public ResponseEntity<List<CompraDTO>> findByDataBefore(@RequestParam LocalDateTime data) {
        return ResponseEntity.ok(service.findByDataBefore(data));
    }

    @GetMapping("/search/observacao")
    public ResponseEntity<List<CompraDTO>> findByObservacaoContaining(@RequestParam String observacao) {
        return ResponseEntity.ok(service.findByObservacaoContaining(observacao));
    }

    @GetMapping("/fornecedor/{idFornecedor}")
    public ResponseEntity<List<CompraDTO>> findByFornecedor_IdFornecedor(@PathVariable Long idFornecedor) {
        return ResponseEntity.ok(service.findByFornecedor_IdFornecedor(idFornecedor));
    }

    @GetMapping("/search/fornecedor")
    public ResponseEntity<List<CompraDTO>> findByFornecedor_NomeContaining(@RequestParam String nome) {
        return ResponseEntity.ok(service.findByFornecedor_NomeContaining(nome));
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> export(@RequestParam Map<String, String> params) {
        List<CompraDTO> compras = service.findAllByCustomMatcher(params);
        Resource resource = downloadService.workbookToResource(excelWritter.export(compras));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=compras.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

    // ACTION

    @PostMapping
    public ResponseEntity<CompraDTO> create(@RequestBody CompraDTO compra) {
        CompraDTO created = service.create(compra);
        URI location = URI.create("/api/compra/" + created.idCompra());

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{idCompra}")
    public ResponseEntity<Void> update(@PathVariable Long idCompra, @RequestBody CompraDTO compra) {
        if (!compra.idCompra().equals(idCompra)) throw new ConflictException("O ID informado no corpo da requisição difere do ID especificado na URL.");
        service.update(idCompra, compra);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idCompra}")
    public ResponseEntity<Void> deleteById(@PathVariable Long idCompra) {
        service.deleteById(idCompra);
        return ResponseEntity.noContent().build();
    }
}
