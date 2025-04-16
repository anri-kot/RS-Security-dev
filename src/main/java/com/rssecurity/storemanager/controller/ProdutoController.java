package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController {
    private ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> create(@RequestBody ProdutoDTO produto) {
        ProdutoDTO created = service.create(produto);
        URI location = URI.create("/api/produto/" + created.idProduto());
        return ResponseEntity.created(location).body(created);
    }
}
