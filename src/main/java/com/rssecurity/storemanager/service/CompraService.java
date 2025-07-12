package com.rssecurity.storemanager.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.rssecurity.storemanager.dto.CompraDTO;
import com.rssecurity.storemanager.dto.ItemCompraDTO;
import com.rssecurity.storemanager.exception.BadRequestException;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.mapper.CompraMapper;
import com.rssecurity.storemanager.mapper.ProdutoMapper;
import com.rssecurity.storemanager.model.Compra;
import com.rssecurity.storemanager.model.ItemCompra;
import com.rssecurity.storemanager.model.Produto;
import com.rssecurity.storemanager.repository.CompraRepository;
import com.rssecurity.storemanager.repository.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service
public class CompraService {
    private final Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "data");

    private final CompraRepository repository;
    private final CompraMapper mapper;
    private final ProdutoRepository produtoRepository;
    
    public CompraService(CompraRepository repository, CompraMapper mapper, ProdutoMapper produtoMapper, ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.produtoRepository = produtoRepository;
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

    public Page<CompraDTO> findAll(int page, int size) {
        Pageable p = PageRequest.of(page, size, SORT_BY_DATE);
        return repository.findAll(p).map(mapper::toDTO);
    }

    public Page<CompraDTO> findAllByCustomMatcher(int page, int size, Map<String, String> filter) {
        filter.values().removeIf(String::isBlank);

        Specification<Compra> spec = CompraSpecification.withFilters(filter);
        Pageable p = PageRequest.of(page, size, SORT_BY_DATE);
        return repository.findAll(spec, p).map(mapper::toDTO);
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

    @Transactional
    public CompraDTO create(CompraDTO compra) {
        if (compra.idCompra() != null) {
            throw new BadRequestException("Campo ID não deve ser fornecido ou deve ser nulo.");
        }

        Compra entity = mapper.toEntity(compra);

        List<ItemCompra> itens = buildItemCompraList(compra, entity, false);

        entity.setItens(itens);
        Compra saved = repository.save(entity);
        updateStock(compra, "CREATE", null);

        return mapper.toDTO(saved);
    }

    @Transactional
    public List<CompraDTO> createAll(List<CompraDTO> compras) {
        List<Long> comprasWithId = compras.stream()
            .filter(c -> c.idCompra() != null && c.idCompra() != 0)
            .map(c -> c.idCompra())
            .toList();
        
            if (!comprasWithId.isEmpty()) {
                throw new BadRequestException("Compra(s) com ID(s) definidos. ID(s): " + comprasWithId);
            }

            Set<Long> allProdutoIds = compras.stream()
                .flatMap(c -> c.itens().stream())
                .map(item -> item.produto().idProduto())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
            Map<Long, Produto> produtosMap = produtoRepository.findAllById(allProdutoIds)
                .stream()
                .collect(Collectors.toMap(Produto::getIdProduto, Function.identity()));

            List<Compra> entities = compras.stream()
                .map(dto -> {
                    Compra entity = mapper.toEntity(dto);
                    List<ItemCompra> itens = buildItemCompraList(dto, entity, produtosMap, false);
                    entity.setItens(itens);
                    return entity;
                })
                .toList();

            List<Compra> created = repository.saveAll(entities);
            updateStockAll(compras);

            return created.stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public void update(Long idCompra, CompraDTO compra) {
        CompraDTO oldCompra = mapper.toDTO(
            repository.findById(idCompra).orElseThrow(
                () -> new ResourceNotFoundException("Compra não encontrada. ID: " + idCompra))
        );
        
        Compra entity = mapper.toEntity(compra);

        List<ItemCompra> itens = buildItemCompraList(compra, entity, true);
        entity.setItens(itens);

        repository.save(entity);
        updateStock(compra, "UPDATE", oldCompra);
    }

    @Transactional
    public void deleteById(Long idCompra) {
        CompraDTO compra = mapper.toDTO(
            repository.findById(idCompra).orElseThrow(
                () -> new ResourceNotFoundException("Compra não encontrada. ID: " + idCompra))
        );

        repository.deleteById(idCompra);
        updateStock(compra, "DELETE", null);
    }

    /**
     * Builds a list of ItemCompra entities from the given CompraDTO.
     * 
     * This method extracts all unique Produto IDs referenced by the item DTOs,
     * fetches the corresponding Produto entities from the repository,
     * and delegates to the overloaded method to complete the mapping.
     *
     * Each item will be linked to the given Compra entity, and optionally have its ID set if it's an update.
     *
     * @param compra the CompraDTO containing the list of item DTOs
     * @param entity the Compra entity to associate with each item
     * @param isUpdate true if the operation is an update (to include item IDs), false for creation
     * @return a list of fully initialized ItemCompra entities ready for persistence
     * @throws ResourceNotFoundException if any referenced Produto ID does not exist
     */
    private List<ItemCompra> buildItemCompraList(CompraDTO compra, Compra entity, boolean isUpdate) {
        List<Long> ids = compra.itens().stream()
                .map(item -> item.produto().idProduto())
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, Produto> produtosMap = produtoRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Produto::getIdProduto, Function.identity()));
        
        return buildItemCompraList(compra, entity, produtosMap, isUpdate);
    }

    /**
     * Builds a list of ItemCompra entities from the given CompraDTO, using a preloaded map of Produto entities.
     * 
     * This variant avoids querying the database by using the provided Map of Produto IDs to entities.
     * It is useful for batch operations where Produto entities have already been fetched.
     *
     * Each item is linked to the given Compra entity, and optionally has its ID set if it's an update.
     *
     * @param compra the CompraDTO containing the list of item DTOs
     * @param entity the Compra entity to associate with each item
     * @param produtosMap a Map of Produto IDs to Produto entities to be used for resolving product references
     * @param isUpdate true if the operation is an update (to include item IDs), false for creation
     * @return a list of fully initialized ItemCompra entities ready for persistence
     * @throws ResourceNotFoundException if any referenced Produto ID is not found in the provided map
     */
    private List<ItemCompra> buildItemCompraList(CompraDTO compra, Compra entity, Map<Long, Produto> produtosMap, boolean isUpdate) {
        return compra.itens().stream()
                .map(itemDto -> {
                    Long idProduto = itemDto.produto().idProduto();
                    Produto produto = produtosMap.get(idProduto);
                    if (produto == null) {
                        throw new ResourceNotFoundException("Produto não encontrado: " + idProduto);
                    }

                    ItemCompra item = new ItemCompra();
                    if (isUpdate) {
                        item.setIdItem(itemDto.idItem());
                    }
                    item.setProduto(produto);
                    item.setQuantidade(itemDto.quantidade());
                    item.setValorUnitario(itemDto.valorUnitario());
                    item.setCompra(entity);
                    
                    return item;
                }).toList();
    }

    // STOCK UPDATE

    public void updateStock(CompraDTO compraDTO, String operation, CompraDTO oldCompraDTO) {
        Map<Long, Integer> stockArranges = new HashMap<>();

        switch (operation) {
            case "CREATE" -> {
                // Adds to the stock
                for (ItemCompraDTO item : compraDTO.itens()) {
                    Long idProduto = item.produto().idProduto();
                    Integer quantidade = item.quantidade();
                    stockArranges.merge(idProduto, quantidade, Integer::sum);
                }
            }

            case "DELETE" -> {
                // Subtracts from the stock
                for (ItemCompraDTO item : compraDTO.itens()) {
                    Long idProduto = item.produto().idProduto();
                    Integer quantidade = item.quantidade();
                    stockArranges.merge(idProduto, -quantidade, Integer::sum);
                }
            }

            case "UPDATE" -> {
                // Subtracts from the stock
                for (ItemCompraDTO item : oldCompraDTO.itens()) {
                    Long idProduto = item.produto().idProduto();
                    Integer quantidade = item.quantidade();
                    stockArranges.merge(idProduto, -quantidade, Integer::sum);
                }
                // Aplica os efeitos da nova compra
                for (ItemCompraDTO item : compraDTO.itens()) {
                    Long idProduto = item.produto().idProduto();
                    Integer quantidade = item.quantidade();
                    stockArranges.merge(idProduto, quantidade, Integer::sum);
                }
            }

            default -> throw new IllegalArgumentException("Operação inválida para atualização de estoque");
        }

        applyUpdateStock(stockArranges);
    }

    private void updateStockAll(List<CompraDTO> compras) {
        Map<Long, Integer> stockArranges = new HashMap<>();

        // Adds to the stock
        compras.forEach(compraDTO -> {
            for (ItemCompraDTO item : compraDTO.itens()) {
                Long idProduto = item.produto().idProduto();
                Integer quantidade = item.quantidade();
                stockArranges.merge(idProduto, quantidade, Integer::sum);
            }
        });

        applyUpdateStock(stockArranges);
    }

    private void applyUpdateStock(Map<Long, Integer> ajustesEstoque) {
        // Aply update
        for (Map.Entry<Long, Integer> ajuste : ajustesEstoque.entrySet()) {
            Long idProduto = ajuste.getKey();
            Integer delta = ajuste.getValue();

            Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + idProduto));

            produto.setEstoque(produto.getEstoque() + delta);
            produtoRepository.save(produto);
        }
    }

}
