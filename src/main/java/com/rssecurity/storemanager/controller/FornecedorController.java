package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    @GetMapping
    public ResponseEntity<List<FornecedorDTO>> findAll() {
        List<FornecedorDTO> fornecedores = service.findAll();
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/{idFornecedor}")
    public ResponseEntity<FornecedorDTO> findById(@PathVariable Long idFornecedor) {
        return ResponseEntity.ok(service.findById(idFornecedor));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FornecedorDTO>> findByNomeContains(@RequestParam(required = false) String nome) {
        return ResponseEntity.ok(service.findByNomeContains(nome));
    }

    @GetMapping("/search/cnpj")
    public ResponseEntity<List<FornecedorDTO>> findByCnpjContains(@RequestParam(required = false) String cnpj) {
        return ResponseEntity.ok(service.findByCnpjContains(cnpj));
    }

    @GetMapping("/search/fone")
    public ResponseEntity<List<FornecedorDTO>> findByTelefoneContains(@RequestParam(required = false) String telefone) {
        return ResponseEntity.ok(service.findByTelefoneContains(telefone));
    }

    @GetMapping("/search/email")
    public ResponseEntity<List<FornecedorDTO>> findByEmailContains(@RequestParam(required = false) String email) {
        return ResponseEntity.ok(service.findByEmailContains(email));
    }

    @PostMapping
    public ResponseEntity createFornecedor(@RequestBody FornecedorDTO fornecedor) {
        FornecedorDTO created = service.create(fornecedor);
        URI location = URI.create("/api/fornecedor/" + created.idFornecedor());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{idFornecedor}")
    public ResponseEntity updateFornecedor(@PathVariable Long idFornecedor, @RequestBody FornecedorDTO fornecedor) {
        if (!fornecedor.idFornecedor().equals(idFornecedor)) {
            throw new ConflictException("O ID informado no corpo da requisição difere do ID especificado na URL.");
        }
        service.update(idFornecedor, fornecedor);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idFornecedor}")
    public ResponseEntity deleteById(@PathVariable Long idFornecedor) {
        service.deleteById(idFornecedor);
        return ResponseEntity.noContent().build();
    }
}
