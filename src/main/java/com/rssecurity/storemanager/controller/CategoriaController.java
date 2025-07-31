package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{idCategoria}")
    public ResponseEntity<CategoriaDTO> findById(@PathVariable Long idCategoria) {
        return ResponseEntity.ok(service.findById(idCategoria));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoriaDTO>> findByNomeContains(@RequestParam String nome) {
        return ResponseEntity.ok(service.findByNomeContains(nome));
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> create(@RequestBody CategoriaDTO categoria) {
        CategoriaDTO created = service.create(categoria);
        URI location = URI.create("api/categoria/" + created.idCategoria());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{idCategoria}")
    public ResponseEntity<Void> update(@PathVariable Long idCategoria, @RequestBody CategoriaDTO categoria) {
        if (!idCategoria.equals(categoria.idCategoria())) {
            throw new ConflictException("O ID informado no corpo da requisição difere do ID especificado na URL. IDs: " + idCategoria + " " + categoria.idCategoria());
        }
        service.update(idCategoria, categoria);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<Void> deleteById(@PathVariable Long idCategoria) {
        service.deleteById(idCategoria);
        return ResponseEntity.noContent().build();
    }
}
