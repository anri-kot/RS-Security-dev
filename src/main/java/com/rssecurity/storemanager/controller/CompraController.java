package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.dto.CompraDTO;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/compra")
public class CompraController {
    @Autowired
    private CompraService service;

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

    // ACTION

    @PostMapping
    public ResponseEntity<CompraDTO> create(@RequestBody CompraDTO compra) {
        CompraDTO created = service.create(compra);
        URI location = URI.create("/api/compra/" + created.idCompra());

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{idCompra}")
    public ResponseEntity update(@PathVariable Long idCompra, @RequestBody CompraDTO compra) {
        if (!compra.idCompra().equals(idCompra)) throw new ConflictException("O ID informado no corpo da requisição difere do ID especificado na URL.");
        service.update(idCompra, compra);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idCompra}")
    public ResponseEntity deleteById(@PathVariable Long idCompra) {
        service.deleteById(idCompra);
        return ResponseEntity.noContent().build();
    }
}
