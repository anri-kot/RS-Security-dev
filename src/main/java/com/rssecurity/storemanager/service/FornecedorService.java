package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.model.Fornecedor;
import com.rssecurity.storemanager.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

@Service
public class FornecedorService {

    private FornecedorRepository repository;

    public FornecedorService(FornecedorRepository repository) {
        this.repository = repository;
    }

    // CRUD

    public void createFornecedor(Fornecedor fornecedor) {
        if (repository.findById(fornecedor.getIdFornecedor()).isPresent()) {
            throw new RuntimeException("Fornecedor já existe. ID: " + fornecedor.getIdFornecedor());
        }
        repository.save(fornecedor);
    }

    public void updateFornecedor(Long id, Fornecedor fornecedor) {
        if(repository.findById(id).isEmpty()) {
            throw new RuntimeException("Fornecedor não encontrado. ID: " + id);
        }
        repository.save(fornecedor);
    }
}
