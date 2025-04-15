package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.exception.ResourceAlreadyExistsException;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.mapper.CategoriaMapper;
import com.rssecurity.storemanager.model.Categoria;
import com.rssecurity.storemanager.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository repository;
    @Autowired
    private CategoriaMapper mapper;

    public CategoriaService(CategoriaRepository repository, CategoriaMapper mapper) {
        repository = this.repository;
        mapper = this.mapper;
    }

    // ==== CRUD ====

    public List<CategoriaDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public CategoriaDTO findById(Long idCategoria) {
        Categoria categoria = repository.findById(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada. ID: " + idCategoria));
        return mapper.toDTO(categoria);
    }
     public List<CategoriaDTO> findByNomeContains(String nome) {
         return repository.findByNomeContains(nome).stream()
                 .map(mapper::toDTO)
                 .toList();
     }

    public CategoriaDTO create(CategoriaDTO categoria) {
        if (categoria.idCategoria() != null) {
            throw new ResourceAlreadyExistsException("Campo ID não deve ser fornecido.");
        }
        Categoria created = repository.save(mapper.toEntity(categoria));
        return mapper.toDTO(created);
    }

    public void update(Long idCategoria, CategoriaDTO categoria) {
        if (repository.findById(idCategoria).isEmpty()) {
            throw new ResourceNotFoundException("Categoria não encontrada. ID: " + idCategoria);
        }
        repository.save(mapper.toEntity(categoria));
    }

    public void delete(Long idCategoria) {
        if (!repository.existsById(idCategoria)) {
            throw new ResourceNotFoundException("Categoria não encontrada. ID: " + idCategoria);
        }
        repository.deleteById(idCategoria);
    }
}
