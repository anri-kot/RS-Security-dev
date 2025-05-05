package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/produto")
public class ProdutoController {
    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{idProduto}")
    public ResponseEntity<ProdutoDTO> findById(@PathVariable Long idProduto) {
        return ResponseEntity.ok(service.findById(idProduto));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProdutoDTO>> findByNomeContains(@RequestParam String nome) {
        return ResponseEntity.ok(service.findByNomeContains(nome));
    }

    @GetMapping("/search/descricao")
    public ResponseEntity<List<ProdutoDTO>> findByDescricaoContains(@RequestParam String descricao) {
        return ResponseEntity.ok(service.findByDescricaoContains(descricao));
    }

    @GetMapping("/search/categoria")
    public ResponseEntity<List<ProdutoDTO>> findByCategoria_Nome(@RequestParam String categoria) {
        return ResponseEntity.ok(service.findByCategoria_Nome(categoria));
    }    

    @PostMapping
    public ResponseEntity<ProdutoDTO> create(@RequestBody @Valid ProdutoDTO produto) {
        ProdutoDTO created = service.create(produto);
        URI location = URI.create("/api/produto/" + created.idProduto());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{idProduto}")
    public ResponseEntity update(@PathVariable Long idProduto, @RequestBody @Valid ProdutoDTO produto) {
        if (!produto.idProduto().equals(idProduto)) {
            throw new ConflictException("O ID informado no corpo da requisição difere do ID especificado na URL.");
        }
        service.update(idProduto, produto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idProduto}")
    public ResponseEntity deleteById(@PathVariable Long idProduto) {
        service.deleteById(idProduto);
        return ResponseEntity.noContent().build();
    }
}
