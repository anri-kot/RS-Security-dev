package com.rssecurity.storemanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rssecurity.storemanager.dto.FornecedorDTO;
import com.rssecurity.storemanager.exception.BadRequestException;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.mapper.FornecedorMapper;
import com.rssecurity.storemanager.model.Fornecedor;
import com.rssecurity.storemanager.repository.FornecedorRepository;

import jakarta.transaction.Transactional;

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
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public FornecedorDTO findById(Long id) {
        Fornecedor fornecedor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado. ID: " + id));
        return mapper.toDTO(fornecedor);
    }

    public List<FornecedorDTO> findByNomeContains(String nome) {
        return repository.findByNomeContains(nome).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<FornecedorDTO> findByCnpjContains(String cnpj) {
        return repository.findByCnpjContains(cnpj).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<FornecedorDTO> findByTelefoneContains(String telefone) {
        return repository.findByTelefoneContains(telefone).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<FornecedorDTO> findByEmailContains(String email) {
        return repository.findByEmailContains(email).stream()
                .map(mapper::toDTO)
                .toList();
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

    // Pages

    public Page<FornecedorDTO> findAll(int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findAll(p).map(mapper::toDTO);
    }

    public Page<FornecedorDTO> findByNomeContains(String nome, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findByNomeContains(nome, p).map(mapper::toDTO);
    }

    public Page<FornecedorDTO> findByCnpjContains(String cnpj, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findByCnpjContains(cnpj, p).map(mapper::toDTO);
    }

    public Page<FornecedorDTO> findByTelefoneContains(String telefone, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findByTelefoneContains(telefone, p).map(mapper::toDTO);
    }

    public Page<FornecedorDTO> findByEmailContains(String email, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findByEmailContains(email, p).map(mapper::toDTO);
    }

    // ACTION

    public FornecedorDTO create(FornecedorDTO fornecedor) {
        if (fornecedor.idFornecedor() != null) {
            throw new BadRequestException("Campo ID não deve ser fornecido ou deve ser nulo.");
        }
        validateCreate(fornecedor);
        Fornecedor entity = mapper.toEntity(fornecedor);
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public List<FornecedorDTO> createAll(List<FornecedorDTO> fornecedores) {
        List<Long> fornecedoresWithId = fornecedores.stream()
                .filter(f -> f.idFornecedor() != null && f.idFornecedor() != 0)
                .map(f -> f.idFornecedor())
                .toList();

        if (!fornecedoresWithId.isEmpty()) {
            throw new BadRequestException("Compra(s) com ID(s) definidos. ID(s): " + fornecedoresWithId);
        }

        List<Fornecedor> created = repository.saveAll(fornecedores.stream()
                .map(mapper::toEntity)
                .toList());

        return created.stream()
                .map(mapper::toDTO)
                .toList();
    }

    public void update(Long id, FornecedorDTO fornecedor) {
        if(repository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Fornecedor não encontrado. ID: " + id);
        }
        validateUpdate(fornecedor);
        Fornecedor entity = mapper.toEntity(fornecedor);
        repository.save(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Fornecedor não encontrado. ID: " + id);
        repository.deleteById(id);
    }

    private void validateCreate(FornecedorDTO fornecedor) {
        if (repository.existsByCnpj(fornecedor.cnpj())) throw new ConflictException("CNPJ já existe: " + fornecedor.cnpj());
        if (repository.existsByTelefone(fornecedor.telefone())) throw new ConflictException("Telefone já existe: " + fornecedor.telefone());
        if (repository.existsByEmail(fornecedor.email())) throw new ConflictException("Email já existe: " + fornecedor.telefone());
    }

    private void validateUpdate(FornecedorDTO fornecedor) {
        // Validade CNPJ
        Optional<Fornecedor> byCnpj = repository.findByCnpj(fornecedor.cnpj());
        if (byCnpj.isPresent() && byCnpj.get().getIdFornecedor() != fornecedor.idFornecedor()) throw new ConflictException("CNPJ já existe: " + fornecedor.cnpj());
        // Validate Telefone
        Optional<Fornecedor> byTelefone = repository.findByTelefone(fornecedor.telefone());
        if (byTelefone.isPresent() && byTelefone.get().getIdFornecedor() != fornecedor.idFornecedor()) throw new ConflictException("Telefone já existe: " + fornecedor.telefone());
        // Validade Email
        Optional<Fornecedor> byEmail = repository.findByEmail(fornecedor.email());
        if (byEmail.isPresent() && byEmail.get().getIdFornecedor() != fornecedor.idFornecedor()) throw new ConflictException("Email já existe: " + fornecedor.telefone());
    }
}
