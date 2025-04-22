package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/venda")
public class VendaController {
    @Autowired
    private VendaService service;

    // SEARCH

    @GetMapping
    public ResponseEntity<List<VendaDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{idVenda}")
    public ResponseEntity<VendaDTO> findById(@PathVariable Long idVenda) {
        return ResponseEntity.ok(service.findById(idVenda));
    }

    @GetMapping("/search/data")
    public ResponseEntity<List<VendaDTO>> findByData(@RequestParam LocalDate data) {
        return ResponseEntity.ok(service.findByData(data));
    }

    @GetMapping("/search/data-between")
    public ResponseEntity<List<VendaDTO>> findByDataBetween(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(service.findByDataBetween(inicio, fim));
    }

    @GetMapping("/search/data-after")
    public ResponseEntity<List<VendaDTO>> findByDataAfter(@RequestParam LocalDateTime data) {
        return ResponseEntity.ok(service.findByDataAfter(data));
    }

    @GetMapping("/search/data-before")
    public ResponseEntity<List<VendaDTO>> findByDataBefore(@RequestParam LocalDateTime data) {
        return ResponseEntity.ok(service.findByDataBefore(data));
    }

    @GetMapping("/search/observacao")
    public ResponseEntity<List<VendaDTO>> findByObservacaoContaining(@RequestParam String observacao) {
        return ResponseEntity.ok(service.findByObservacaoContaining(observacao));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<VendaDTO>> findByUsuario_IdUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.findByUsuario_IdUsuario(idUsuario));
    }

    @GetMapping("/search/usuario")
    public ResponseEntity<List<VendaDTO>> findByUsuario_NomeContainingOrUsuario_SobrenomeContaining(@RequestParam String nome) {
        return ResponseEntity.ok(service.findByUsuario_NomeContainingOrUsuario_SobrenomeContaining(nome));
    }

    @GetMapping("/search/usuario/username")
    public ResponseEntity<List<VendaDTO>> findByUsuario_Username(@RequestParam String username) {
        return ResponseEntity.ok(service.findByUsuario_Username(username));
    }

    // ACTIONS

    @PostMapping
    public ResponseEntity<VendaDTO> create(@RequestBody VendaDTO venda) {
        VendaDTO created = service.create(venda);
        URI location = URI.create("/api/venda/" + created.idVenda());

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{idVenda}")
    public ResponseEntity<VendaDTO> update(@PathVariable Long idVenda, @RequestBody VendaDTO venda) {
        if (!venda.idVenda().equals(idVenda)) {
            throw new ConflictException("O ID informado no corpo da requisição difere do ID especificado na URL.");
        }
        service.update(idVenda, venda);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idVenda}")
    public ResponseEntity<VendaDTO> deleteById(@PathVariable Long idVenda) {
        service.deleteById(idVenda);
        return ResponseEntity.noContent().build();
    }
}
