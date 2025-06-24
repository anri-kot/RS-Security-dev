package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.dto.CompraDTO;
import com.rssecurity.storemanager.exception.BadRequestException;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.mapper.CompraMapper;
import com.rssecurity.storemanager.mapper.ProdutoMapper;
import com.rssecurity.storemanager.model.Compra;
import com.rssecurity.storemanager.model.ItemCompra;
import com.rssecurity.storemanager.repository.CompraRepository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CompraService {
    private final Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "data");

    private final CompraRepository repository;
    private final CompraMapper mapper;
    private final ProdutoMapper produtoMapper;

    public CompraService(CompraRepository repository, CompraMapper mapper, ProdutoMapper produtoMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.produtoMapper = produtoMapper;
    }

    // SEARCH

    public List<CompraDTO> findAll() {
        return repository.findAll(SORT_BY_DATE).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<CompraDTO> findAllByCustomMatcher(Map<String, String> filter) {
        filter.values().removeIf(String::isBlank);

        Specification<Compra> spec = CompraSpecification.withFilters(filter);
        return repository.findAll(spec, SORT_BY_DATE).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public CompraDTO findById(Long idCompra) {
        Compra compra = repository.findById(idCompra)
                .orElseThrow(() -> new ResourceNotFoundException("Compra não encontrada. ID: " + idCompra));
        return mapper.toDTO(compra);
    }

    public List<CompraDTO> findByData(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = inicio.plusDays(1);
        return repository.findByDataBetween(inicio, fim).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<CompraDTO> findByDataBetween(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findByDataBetween(inicio, fim).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<CompraDTO> findByDataAfter(LocalDateTime data) {
        return repository.findByDataAfter(data).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<CompraDTO> findByDataBefore(LocalDateTime data) {
        return repository.findByDataBefore(data).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<CompraDTO> findByObservacaoContaining(String observacao) {
        return repository.findByObservacaoContaining(observacao).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<CompraDTO> findByFornecedor_IdFornecedor(Long idFornecedor) {
        return repository.findByFornecedor_IdFornecedor(idFornecedor).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<CompraDTO> findByFornecedor_NomeContaining(String nome) {
        return repository.findByFornecedor_NomeContaining(nome).stream()
                .map(mapper::toDTO)
                .toList();
    }

    // ACTIONS

    public CompraDTO create(CompraDTO dto) {
        if (dto.idCompra() != null) {
            throw new BadRequestException("Campo ID não deve ser fornecido ou deve ser nulo.");
        }

        Compra entity = mapper.toEntity(dto);

        List<ItemCompra> itens = getItens(dto, entity, false);

        entity.setItens(itens);
        Compra saved = repository.save(entity);

        return mapper.toDTO(saved);
    }

    public void update(Long idCompra, CompraDTO compra) {
        if (!repository.existsById(idCompra)) {
            throw new ResourceNotFoundException("Compra não encontrada. ID: " + idCompra);
        }

        Compra entity = mapper.toEntity(compra);

        List<ItemCompra> itens = getItens(compra, entity, true);
        entity.setItens(itens);
        repository.save(entity);
    }

    public void deleteById(Long idCompra) {
        if (!repository.existsById(idCompra)) {
            throw new ResourceNotFoundException("Compra não encontrada. ID: " + idCompra);
        }
        repository.deleteById(idCompra);
    }

    private List<ItemCompra> getItens(CompraDTO dto, Compra entity, boolean isUpdate) {
        List<ItemCompra> itens = dto.itens().stream()
                .map(itemDto -> {
                    ItemCompra item = new ItemCompra();
                    if (isUpdate) {
                        item.setidItem(itemDto.idItem());
                    }
                    item.setQuantidade(itemDto.quantidade());
                    item.setValorUnitario(itemDto.valorUnitario());
                    item.setProduto(produtoMapper.toEntity(itemDto.produto()));
                    item.setCompra(entity);
                    return item;
                }).toList();

        return itens;
    }

}
