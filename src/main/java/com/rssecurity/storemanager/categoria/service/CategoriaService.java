package com.rssecurity.storemanager.categoria.service;

import com.rssecurity.storemanager.categoria.dto.CategoriaDTO;
import com.rssecurity.storemanager.categoria.mapper.CategoriaMapper;
import com.rssecurity.storemanager.categoria.model.Categoria;
import com.rssecurity.storemanager.categoria.repository.CategoriaRepository;
import com.rssecurity.storemanager.infra.exception.BadRequestException;
import com.rssecurity.storemanager.infra.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<CategoriaDTO> findAll(int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findAll(p).map(mapper::toDTO);
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

    public Page<CategoriaDTO> findByNomeContains(String nome, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findByNomeContains(nome, p).map(mapper::toDTO);
    }

    public CategoriaDTO create(CategoriaDTO categoria) {
        if (categoria.idCategoria() != null) {
            throw new BadRequestException("Campo ID não deve ser fornecido ou deve ser nulo.");
        }
        Categoria created = repository.save(mapper.toEntity(categoria));
        return mapper.toDTO(created);
    }

    @Transactional
    public List<CategoriaDTO> createAll(List<CategoriaDTO> categorias) {
        List<Long> categoriasWithId = categorias.stream()
                .filter(c -> c.idCategoria() != null && c.idCategoria() != 0)
                .map(c -> c.idCategoria())
                .toList();

        if (!categoriasWithId.isEmpty()) {
        }

        List<Categoria> created = repository.saveAll(categorias.stream()
                .map(mapper::toEntity)
                .toList());
        return created.stream()
                .map(mapper::toDTO)
                .toList();
    }

    public void update(Long idCategoria, CategoriaDTO categoria) {
        if (!repository.existsById(idCategoria)) {
            throw new ResourceNotFoundException("Categoria não encontrada. ID: " + idCategoria);
        }
        repository.save(mapper.toEntity(categoria));
    }

    public void deleteById(Long idCategoria) {
        if (!repository.existsById(idCategoria)) {
            throw new ResourceNotFoundException("Categoria não encontrada. ID: " + idCategoria);
        }
        repository.deleteById(idCategoria);
    }
}
