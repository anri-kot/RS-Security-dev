package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.model.Fornecedor;
import com.rssecurity.storemanager.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    private FornecedorRepository repository;

    public FornecedorService(FornecedorRepository repository) {
        this.repository = repository;
    }

    // ====== CRUD =======

    // SEARCH

    public List<Fornecedor> findAll() {
        return repository.findAll();
    }

    public Fornecedor findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado. ID: " + id));
    }

    public List<Fornecedor> findByName(String name) {
        return repository.findByName(name);
    }

    public Fornecedor findByCnpj(String cnpj) {
        return repository.findByCnpj(cnpj);
    }

    public Fornecedor findByTelefone(String telefone) {
        return repository.findByTelefone(telefone);
    }

    public Fornecedor findByEmail(String email) {
        return repository.findByEmail(email);
    }

    // ACTION

    public void create(Fornecedor fornecedor) {
        if (repository.findById(fornecedor.getIdFornecedor()).isPresent()) {
            throw new RuntimeException("Fornecedor já existe. ID: " + fornecedor.getIdFornecedor());
        }
        repository.save(fornecedor);
    }

    public void update(Long id, Fornecedor fornecedor) {
        if(repository.findById(id).isEmpty()) {
            throw new RuntimeException("Fornecedor não encontrado. ID: " + id);
        }
        repository.save(fornecedor);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
