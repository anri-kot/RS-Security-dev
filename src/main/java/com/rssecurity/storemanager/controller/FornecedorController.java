package com.rssecurity.storemanager.controller;

import com.rssecurity.storemanager.model.Fornecedor;
import com.rssecurity.storemanager.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    public FornecedorController(FornecedorService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createFornecedor(@RequestBody Fornecedor fornecedor) {
        service.createFornecedor(fornecedor);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFornecedor(@RequestBody Fornecedor fornecedor) {
        service.updateFornecedor(fornecedor.getIdFornecedor(), fornecedor);
    }
}
