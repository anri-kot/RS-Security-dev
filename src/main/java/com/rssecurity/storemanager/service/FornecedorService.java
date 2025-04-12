package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.mapper.FornecedorMapper;
import com.rssecurity.storemanager.model.Fornecedor;
import com.rssecurity.storemanager.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository repository;
    @Autowired
    private FornecedorMapper mapper;

    public FornecedorService(FornecedorRepository repository) {
        this.repository = repository;
    }

    // ====== CRUD =======

    // SEARCH

    public List<FornecedorDTO> findAll() {
        List<FornecedorDTO> fornecedores = repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
        return fornecedores;
    }

    public FornecedorDTO findById(Long id) {
        Fornecedor fornecedor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado. ID: " + id));
        return mapper.toDTO(fornecedor);
    }

    public List<FornecedorDTO> findByNome(String nome) {
        List<FornecedorDTO> fornecedores = repository.findByNomeContains(nome).stream()
                .map(mapper::toDTO)
                .toList();
        return fornecedores;
    }

    public FornecedorDTO findByCnpj(String cnpj) {
        Fornecedor fornecedor = repository.findByCnpj(cnpj)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado. CNPJ: " + cnpj));
        return mapper.toDTO(fornecedor);
    }

    public FornecedorDTO findByTelefone(String telefone) {
        Fornecedor fornecedor = repository.findByTelefone(telefone)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado. Telefone: " + telefone));
        return mapper.toDTO(fornecedor);
    }

    public FornecedorDTO findByEmail(String email) {
        Fornecedor fornecedor = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado. Email: " + email));
        return mapper.toDTO(fornecedor);
    }

    // ACTION

    public FornecedorDTO create(FornecedorDTO fornecedor) {
        validate(fornecedor);
        Fornecedor entity = mapper.toEntity(fornecedor);
        FornecedorDTO saved = mapper.toDTO(repository.save(entity));
        return saved;
    }

    public void update(Long id, FornecedorDTO fornecedor) {
        if(repository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Fornecedor não encontrado. ID: " + id);
        }
        validate(fornecedor);
        Fornecedor entity = mapper.toEntity(fornecedor);
        repository.save(entity);
    }

    public void deleteById(Long id) {
        if (repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Fornecedor não encontrado. ID: " + id);
        repository.deleteById(id);
    }

    private void validate(FornecedorDTO fornecedor) {
        if (repository.existsByCnpj(fornecedor.cnpj())) throw new ConflictException("CNPJ ja existe: " + fornecedor.cnpj());
        if (repository.existsByTelefone(fornecedor.telefone())) throw new ConflictException("Telefone ja existe: " + fornecedor.telefone());
        if (repository.existsByEmail(fornecedor.email())) throw new ConflictException("Email ja existe: " + fornecedor.telefone());
    }
}
