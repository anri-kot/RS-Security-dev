package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.exception.BadRequestException;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.mapper.ProdutoMapper;
import com.rssecurity.storemanager.model.Produto;
import com.rssecurity.storemanager.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    public ProdutoService(ProdutoRepository repository, ProdutoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // SEARCH

    public List<ProdutoDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public ProdutoDTO findById(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado. ID: " + id));
        return mapper.toDTO(produto);
    }

    public List<ProdutoDTO> findByNomeContains(String nome) {
        return repository.findByNomeContains(nome).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<ProdutoDTO> findByDescricaoContains(String descricao) {
        return repository.findByDescricaoContains(descricao).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<ProdutoDTO> findByCategoria_Nome(String categoria) {
        return repository.findByCategoria_Nome(categoria).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<ProdutoDTO> findByNomeContainsIgnoreCaseAndCategoria_IdCategoria(String termo, Long categoria) {
        return repository.findByNomeContainsIgnoreCaseAndCategoria_IdCategoria(termo, categoria).stream()
                .map(mapper::toDTO)
                .toList();
    }

    // ACTIONS

    public ProdutoDTO create(ProdutoDTO produto) {
        if (produto.idProduto() != null) {
            throw new BadRequestException("Campo ID não deve ser fornecido ou deve ser nulo.");
        }
        Produto created = repository.save(mapper.toEntity(produto));
        return mapper.toDTO(created);
    }

    public void update(Long id, ProdutoDTO produto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado. ID: " + id);
        }
        repository.save(mapper.toEntity(produto));
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado. ID: " + id);
        }
        repository.deleteById(id);
    }

    public List<ProdutoDTO> findByIdProdutoContainsAndCategoriaIdCategoria(Long termoId, Long idCategoria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByIdProdutoContainsAndCategoriaIdCategoria'");
    }
}
