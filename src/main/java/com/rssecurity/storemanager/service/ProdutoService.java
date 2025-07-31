package com.rssecurity.storemanager.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.exception.BadRequestException;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.mapper.ProdutoMapper;
import com.rssecurity.storemanager.model.Produto;
import com.rssecurity.storemanager.repository.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    // SEARCH

    public ProdutoService(ProdutoRepository repository, ProdutoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

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

    public ProdutoDTO findByCodigoBarras(String codigoBarras) {
        Produto produto = repository.findByCodigoBarras(codigoBarras)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado. Código de barras: " + codigoBarras));
        return mapper.toDTO(produto);
    }

    public List<ProdutoDTO> findByCodigoBarrasContains(String codigoBarras) {
        return repository.findByCodigoBarrasContains(codigoBarras).stream()
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

    // PAGES

    public Page<ProdutoDTO> findAll(int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findAll(p).map(mapper::toDTO);
    }

    public Page<ProdutoDTO> findByNomeContains(String nome, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findByNomeContains(nome, p).map(mapper::toDTO);
    }

    public Page<ProdutoDTO> findByCodigoBarrasContains(String codigo, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findByCodigoBarrasContains(codigo, p).map(mapper::toDTO);
    }

    public Page<ProdutoDTO> findByDescricaoContains(String descricao, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findByDescricaoContains(descricao, p).map(mapper::toDTO);
    }

    public Page<ProdutoDTO> findByCategoria_Nome(String categoria, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findByCategoria_Nome(categoria, p).map(mapper::toDTO);
    }

    public Page<ProdutoDTO> findByNomeContainsIgnoreCaseAndCategoria_IdCategoria(String termo, Long categoria, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        return repository.findByNomeContainsIgnoreCaseAndCategoria_IdCategoria(termo, categoria, p).map(mapper::toDTO);
    }

    // ACTIONS

    @Transactional
    public ProdutoDTO create(ProdutoDTO produto) {
        if (produto.idProduto() != null) {
            throw new BadRequestException("Campo ID não deve ser fornecido ou deve ser nulo.");
        }
        Produto created = repository.save(mapper.toEntity(produto));
        return mapper.toDTO(created);
    }

    @Transactional
    public List<ProdutoDTO> createAll(List<ProdutoDTO> produtos) {
        List<String> produtosWithId = produtos.stream()
                .filter(p -> p.idProduto() != null && p.idProduto() != 0)
                .map(ProdutoDTO::nome)
                .toList();

        if (!produtosWithId.isEmpty()) {
            throw new BadRequestException("Alguns produtos têm ID definido: " + produtosWithId);
        }

        List<Produto> entities = produtos.stream()
                .map(mapper::toEntity)
                .toList();

        List<Produto> created = repository.saveAll(entities);

        return created.stream().map(mapper::toDTO).toList();
    }

    @Transactional
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
}
