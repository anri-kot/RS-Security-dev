package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.model.Fornecedor;
import com.rssecurity.storemanager.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    public FornecedorController(FornecedorService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Fornecedor findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/{nome}")
    public List<Fornecedor> findByName(@PathVariable String nome) {
        return service.findByName(nome);
    }

    @GetMapping("/{cnpj}")
    public Fornecedor findByCnpj(@PathVariable String cnpj) {
        return service.findByCnpj(cnpj);
    }

    @GetMapping("/{telefone}")
    public Fornecedor findByTelefone(@PathVariable String telefone) {
        return service.findByTelefone(telefone);
    }

    @GetMapping("/{email}")
    public Fornecedor findByEmail(@PathVariable String email) {
        return service.findByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createFornecedor(@RequestBody Fornecedor fornecedor) {
        service.create(fornecedor);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFornecedor(@PathVariable Fornecedor fornecedor) {
        service.update(fornecedor.getIdFornecedor(), fornecedor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
}
