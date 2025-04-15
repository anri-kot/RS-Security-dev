package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.exception.ResourceAlreadyExistsException;
import com.rssecurity.storemanager.mapper.CategoriaMapper;
import com.rssecurity.storemanager.model.Categoria;
import com.rssecurity.storemanager.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public CategoriaDTO create(CategoriaDTO categoria) {
        if (categoria.id() != null) {
            throw new ResourceAlreadyExistsException("Campo ID não deve ser fornecido.");
        }
        Categoria created = repository.save(mapper.toEntity(categoria));
        return mapper.toDTO(created);
    }
}
