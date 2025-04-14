package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.mapper.FornecedorMapper;
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
    public ResponseEntity<List<FornecedorDTO>> findByNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.findByNome(nome));
    }

    @GetMapping("/search/cnpj")
    public ResponseEntity<FornecedorDTO> findByCnpj(@RequestParam String cnpj) {
        return ResponseEntity.ok(service.findByCnpj(cnpj));
    }

    @GetMapping("/search/fone")
    public ResponseEntity<FornecedorDTO> findByTelefone(@RequestParam String telefone) {
        return ResponseEntity.ok(service.findByTelefone(telefone));
    }

    @GetMapping("/search/email")
    public ResponseEntity<FornecedorDTO> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity createFornecedor(@RequestBody FornecedorDTO fornecedor) {
        FornecedorDTO created = service.create(fornecedor);
        URI location = URI.create("/api/fornecedor/" + created.idFornecedor());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{idFornecedor}")
    public ResponseEntity updateFornecedor(@PathVariable Long idFornecedor, @RequestBody FornecedorDTO fornecedor) {
        service.update(idFornecedor, fornecedor);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idFornecedor}")
    public ResponseEntity deleteById(@PathVariable Long idFornecedor) {
        service.deleteById(idFornecedor);
        return ResponseEntity.noContent().build();
    }
}
