package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.dto.CompraDTO;
import com.rssecurity.storemanager.service.CompraService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/compra")
public class CompraController {
    @Autowired
    private CompraService service;

    @GetMapping
    public ResponseEntity<List<CompraDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{idCompra}")
    public ResponseEntity<CompraDTO> findById(@PathVariable Long idCompra) {
        return ResponseEntity.ok(service.findById(idCompra));
    }

    @PostMapping
    public ResponseEntity<CompraDTO> create(@RequestBody CompraDTO compra) {
        CompraDTO created = service.create(compra);
        URI location = URI.create("/api/compra/" + created.idCompra());

        return ResponseEntity.created(location).body(created);
    }
}
