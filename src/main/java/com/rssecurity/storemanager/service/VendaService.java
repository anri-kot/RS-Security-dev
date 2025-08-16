package com.rssecurity.storemanager.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.springframework.transaction.annotation.Transactional;

import com.rssecurity.storemanager.dto.ItemVendaDTO;
import com.rssecurity.storemanager.dto.UsuarioResumoDTO;
import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.exception.BadRequestException;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.mapper.ProdutoMapper;
import com.rssecurity.storemanager.mapper.VendaMapper;
import com.rssecurity.storemanager.model.ItemVenda;
import com.rssecurity.storemanager.model.Produto;
import com.rssecurity.storemanager.model.Usuario;
import com.rssecurity.storemanager.model.Venda;
import com.rssecurity.storemanager.repository.ProdutoRepository;
import com.rssecurity.storemanager.repository.UsuarioRepository;
import com.rssecurity.storemanager.repository.VendaRepository;

@Service
public class VendaService {
    private final Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "data");

    private final VendaRepository repository;
    private final VendaMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    
    public VendaService(VendaRepository repository, VendaMapper mapper, ProdutoMapper produtoMapper,
            UsuarioRepository usuarioRepository, ProdutoRepository produtoRepository, ProdutoService produtoService) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
    }

    // SEARCH

    public List<VendaDTO> findAll() {
        return repository.findAll(SORT_BY_DATE).stream()
                .map(mapper::toDTO)
                .toList();
    }

    /**
     * @param filter:
     * - tipo: can be 'usuario', 'produtoNome', 'produtoId' or 'codigo', where usuario searches for username and codigo searches for codigoBarras
     * - termo: queue for the selected 'tipo'
     * - observacao
     * - metodoPagamento
     * - dataInicio
     * - dataFim
     * - data: searches for 'venda' registers in a specific date
    */
    public List<VendaDTO> findAllByCustomMatcher(Map<String, String> filter) {
        filter.values().removeIf(String::isBlank);

        Specification<Venda> spec = VendaSpecification.withFilters(filter);
        return repository.findAll(spec, SORT_BY_DATE).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public Page<VendaDTO> findAll(int pageNumber, int pageSize) {
        Pageable p = PageRequest.of(pageNumber, pageSize, SORT_BY_DATE);
        return repository.findAll(p).map(mapper::toDTO);
    }

    public Page<VendaDTO> findAllByCustomMatcher(int pageNumber, int pageSize, Map<String, String> filter) {
        filter.values().removeIf((String::isBlank));

        Specification<Venda> spec = VendaSpecification.withFilters(filter);
        Pageable p = PageRequest.of(pageNumber, pageSize, SORT_BY_DATE);
        return repository.findAll(spec, p).map(mapper::toDTO);
    }

    public VendaDTO findById(Long idVenda) {
        Venda venda = repository.findById(idVenda)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada. ID: " + idVenda));
        return mapper.toDTO(venda);
    }

    public List<VendaDTO> findByData(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = inicio.plusDays(1);
        return repository.findByDataBetween(inicio, fim).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<VendaDTO> findByDataBetween(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findByDataBetween(inicio, fim).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<VendaDTO> findByDataAfter(LocalDateTime data) {
        return repository.findByDataAfter(data).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<VendaDTO> findByDataBefore(LocalDateTime data) {
        return repository.findByDataBefore(data).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<VendaDTO> findByObservacaoContaining(String observacao) {
        return repository.findByObservacaoContaining(observacao).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<VendaDTO> findByUsuario_IdUsuario(Long idUsuario) {
        return repository.findByUsuario_IdUsuario(idUsuario).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<VendaDTO> findByUsuario_Username(String nome) {
        return repository.findByUsuario_Username(nome).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<VendaDTO> findByUsuario_NomeContainingOrUsuario_SobrenomeContaining(String nome) {
        return repository.findByUsuario_NomeContainingOrUsuario_SobrenomeContaining(nome, nome).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public BigDecimal calculateTotalVendaValueBetween(LocalDateTime start, LocalDateTime end) {
        return repository.calculateTotalVendaValueBetween(start, end);
    }

    // ACTIONS

    // Overloaded method for PDV transactions
    public VendaDTO create(VendaDTO venda, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado. Username: " + username));
        UsuarioResumoDTO usu = new UsuarioResumoDTO(usuario.getIdUsuario(), usuario.getUsername(), usuario.getNome(),
                usuario.getSobrenome());

        BigDecimal troco = BigDecimal.ZERO;
        if (venda.metodoPagamento().equals("DINHEIRO")) {
            troco = venda.valorRecebido().subtract(venda.getTotal());
        }

        VendaDTO dto = new VendaDTO(
                venda.idVenda(),
                venda.data(),
                venda.observacao(),
                usu,
                venda.itens(),
                venda.metodoPagamento(),
                venda.valorRecebido(),
                troco);
        return create(dto);
    }

    @Transactional
    public VendaDTO create(VendaDTO venda) {
        if (venda.idVenda() != null) {
            throw new BadRequestException("Campo ID não deve ser fornecido ou deve ser nulo.");
        }
        validateUsuario(venda.usuario());
        if (venda.metodoPagamento().equals("DINHEIRO")) {
            validateTroco(venda);
        }

        Venda entity = mapper.toEntity(venda);
        List<ItemVenda> itens = buildItemVendaList(venda, entity, false);

        entity.setItens(itens);
        VendaDTO saved = mapper.toDTO(repository.save(entity));

        updateStock(venda, "CREATE", null);

        return saved;
    }

    @Transactional
    public List<VendaDTO> createAll(List<VendaDTO> vendas) {
        // Checking if IDs are not null
        List<Long> vendasWithId = vendas.stream()
                .filter(v -> v.idVenda() != null && v.idVenda() != 0)
                .map(v -> v.idVenda())
                .toList();

        if (!vendasWithId.isEmpty()) {
            throw new BadRequestException("Categoria(s) com ID(s) definido(s). ID(s): " + vendasWithId);
        }

        // Validating users
        Map<String, Usuario> usuarios = getAllUsuarios(vendas.stream()
                .map(v -> v.usuario())
                .toList());

        Set<Long> allProdutoIds = vendas.stream()
                .flatMap(v -> v.itens().stream())
                .map(item -> item.produto().idProduto())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, Produto> produtosMap = produtoRepository.findAllById(allProdutoIds)
            .stream()
            .collect(Collectors.toMap(Produto::getIdProduto, Function.identity()));

        List<Venda> entities = vendas.stream()
                .map(dto -> {
                        Venda entity = mapper.toEntity(dto);
                        entity.setUsuario(usuarios.get(dto.usuario().username()));
                        List<ItemVenda> itens = buildItemVendaList(dto, entity, produtosMap, false);
                        entity.setItens(itens);
                        return entity;
                    })
                    .toList();

        List<Venda> created = repository.saveAll(entities);
        updateStockAll(vendas);

        return created.stream()
                .map(mapper::toDTO)
                .toList();

    }

    @Transactional
    public void update(Long idVenda, VendaDTO venda) {
        VendaDTO oldVenda = findById(idVenda);

        validateUsuario(venda.usuario());
        if (venda.metodoPagamento().equals("DINHEIRO")) {
            validateTroco(venda);
        }

        Venda entity = mapper.toEntity(venda);

        List<ItemVenda> itens = buildItemVendaList(venda, entity, true);
        entity.setItens(itens);

        VendaDTO saved = mapper.toDTO(repository.save(entity));
        updateStock(saved, "UPDATE", oldVenda);
    }

    @Transactional
    public void deleteById(Long idVenda) {
        if (!repository.existsById(idVenda)) {
            throw new ResourceNotFoundException("Venda não encontrada. ID: " + idVenda);
        }

        repository.deleteById(idVenda);
    }

    // STOCK UPDATE

    public void updateStock(VendaDTO vendaDTO, String operation, VendaDTO oldVendaDTO) {
        Map<Long, Integer> stockArranges = new HashMap<>();

        switch (operation) {
            case "CREATE" -> {
                // Adds to the stock
                for (ItemVendaDTO item : vendaDTO.itens()) {
                    Long idProduto = item.produto().idProduto();
                    Integer quantidade = item.quantidade();
                    stockArranges.merge(idProduto, -quantidade, Integer::sum);
                }
            }

            case "DELETE" -> {
                // Subtracts from the stock
                for (ItemVendaDTO item : vendaDTO.itens()) {
                    Long idProduto = item.produto().idProduto();
                    Integer quantidade = item.quantidade();
                    stockArranges.merge(idProduto, -quantidade, Integer::sum);
                }
            }

            case "UPDATE" -> {
                // Subtracts from the stock
                for (ItemVendaDTO item : oldVendaDTO.itens()) {
                    if (item.produto() != null) {
                        Long idProduto = item.produto().idProduto();
                        Integer quantidade = item.quantidade();
                        stockArranges.merge(idProduto, quantidade, Integer::sum);
                    }
                }
                // Apply
                for (ItemVendaDTO item : vendaDTO.itens()) {
                    Long idProduto = item.produto().idProduto();
                    Integer quantidade = item.quantidade();
                    stockArranges.merge(idProduto, -quantidade, Integer::sum);
                }
            }

            default -> throw new IllegalArgumentException("Operação inválida para atualização de estoque");
        }

        applyUpdateStock(stockArranges);
    }

    private void updateStockAll(List<VendaDTO> vendas) {
        Map<Long, Integer> stockArranges = new HashMap<>();

        // Adds to the stock
        vendas.forEach(vendaDTO -> {
            for (ItemVendaDTO item : vendaDTO.itens()) {
                Long idProduto = item.produto().idProduto();
                Integer quantidade = item.quantidade();
                stockArranges.merge(idProduto, quantidade, Integer::sum);
            }
        });

        applyUpdateStock(stockArranges);
    }

    private void applyUpdateStock(Map<Long, Integer> stockArranges) {
        for (Map.Entry<Long, Integer> ajuste : stockArranges.entrySet()) {
            Long idProduto = ajuste.getKey();
            Integer delta = ajuste.getValue();

            Produto produto = produtoRepository.findById(idProduto)
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + idProduto));

            produto.setEstoque(produto.getEstoque() + delta);
            produtoRepository.save(produto);
        }
    }

    // private void updateStock(VendaDTO newVenda) {
    // updateStock(null, newVenda);
    // }

    // private void updateStock(VendaDTO oldVenda, VendaDTO newVenda) {
    // Map<Long, Integer> oldQuantidades = new HashMap<>();

    // // Old quantities (stock reposition, hence the negative value)
    // if (oldVenda != null) {
    // for (ItemVendaDTO item : oldVenda.itens()) {
    // long produtoId = item.produto().idProduto();
    // oldQuantidades.put(produtoId, item.quantidade());
    // }
    // }

    // for (ItemVendaDTO item : newVenda.itens()) {
    // long produtoId = item.produto().idProduto();
    // int oldQty = oldQuantidades.getOrDefault(produtoId, 0);
    // int newQty = item.quantidade();
    // int stockDifference = oldQty - newQty; // positive value = refill stock,
    // negative = remove from stock

    // Produto produto = produtoMapper.toEntity(item.produto());
    // produto.setEstoque(produto.getEstoque() + stockDifference);

    // produtoService.update(produtoId, produtoMapper.toDTO(produto));
    // }
    // }

    /**
     * Builds a list of ItemVenda entities from the given VendaDTO.
     * 
     * This method extracts all unique Produto IDs referenced by the item DTOs,
     * fetches the corresponding Produto entities from the repository,
     * and delegates to the overloaded method to complete the mapping.
     *
     * Each item will be linked to the given Venda entity, and optionally have its ID set if it's an update.
     *
     * @param venda the VendaDTO containing the list of item DTOs
     * @param entity the Venda entity to associate with each item
     * @param isUpdate true if the operation is an update (to include item IDs), false for creation
     * @return a list of fully initialized ItemVenda entities ready for persistence
     * @throws ResourceNotFoundException if any referenced Produto ID does not exist
     */
    private List<ItemVenda> buildItemVendaList(VendaDTO venda, Venda entity, boolean isUpdate) {
        List<Long> ids = venda.itens().stream()
                .map(item -> item.produto().idProduto())
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        
        Map<Long, Produto> produtosMap = produtoRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Produto::getIdProduto, Function.identity()));
        
        return buildItemVendaList(venda, entity, produtosMap, isUpdate);
    }

    /**
     * Builds a list of ItemVenda entities from the given VendaDTO, using a preloaded map of Produto entities.
     * 
     * This variant avoids querying the database by using the provided Map of Produto IDs to entities.
     * It is useful for batch operations where Produto entities have already been fetched.
     *
     * Each item is linked to the given Venda entity, and optionally has its ID set if it's an update.
     *
     * @param venda    the VendaDTO containing the list of item DTOs
     * @param entity   the Venda entity to associate with each item
     * @param produtosMap a Map of Produto IDs to Produto entities to be used for resolving product references
     * @param isUpdate true if the operation is an update (to include item IDs), false for creation
     * @return a list of fully initialized ItemVenda entities ready for persistence
     * @throws ResourceNotFoundException if any referenced Produto ID does not exist
     */
    private List<ItemVenda> buildItemVendaList(VendaDTO venda, Venda entity, Map<Long, Produto> produtosMap, boolean isUpdate) {
        return venda.itens().stream()
                .map(itemDto -> {
                    Long idProduto = itemDto.produto().idProduto();
                    Produto produto = produtosMap.get(idProduto);
                    if (produto == null) {
                        throw new ResourceNotFoundException("Produto não encontrado: " + idProduto);
                    }

                    ItemVenda item = new ItemVenda();
                    if (isUpdate) {
                        item.setIdItem(itemDto.idItem());
                    }
                    item.setProduto(produto);
                    item.setQuantidade(itemDto.quantidade());
                    item.setValorUnitario(itemDto.valorUnitario());
                    item.setDesconto(itemDto.desconto());
                    item.setVenda(entity);

                    validateProduto(produto, item);

                    return item;
                }).toList();
    }

    private Map<String, Usuario> getAllUsuarios(List<UsuarioResumoDTO> usuarios) {
        List<String> usernames = usuarios.stream()
            .map(UsuarioResumoDTO::username)
            .distinct()
            .toList();

        Map<String, Usuario> foundMap = usuarioRepository.findAllByUsernameIn(usernames).stream()
            .collect(Collectors.toMap(Usuario::getUsername, Function.identity()));

        List<String> notFound = usernames.stream()
            .filter(username -> !foundMap.containsKey(username))
            .toList();

        if (!notFound.isEmpty()) {
            throw new BadRequestException("Users not found: " + notFound);
        }

        return foundMap;
    }

    private void validateUsuario(UsuarioResumoDTO usuario) {
        Usuario theUsuario = usuarioRepository.findById(usuario.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado. ID: " + usuario.idUsuario()));;

        // Compares the inputed username with the register in the database
        if (!theUsuario.getUsername().equals(usuario.username())) {
            throw new ConflictException(
                    "Username inserido não corresponde ao ID do usuario no banco de dados. ID e Usuario informados: "
                            + usuario.idUsuario() + " | " + usuario.username());
        }
    }

    // TODO: Assign MIN_STOCK to user preferences
    private void validateProduto(Produto produtoInStock, ItemVenda item) {
        final int MIN_STOCK = 1;
        if ((produtoInStock.getEstoque() - item.getQuantidade()) < MIN_STOCK) {
            throw new BadRequestException("Estoque insuficiente para o produto: " + produtoInStock.getNome());
        }
    }

    private void validateTroco(VendaDTO venda) {
        BigDecimal total = venda.getTotal().setScale(2, RoundingMode.HALF_UP);
        BigDecimal received = venda.valorRecebido().setScale(2, RoundingMode.HALF_UP);
        BigDecimal troco = venda.troco().setScale(2, RoundingMode.HALF_UP);

        BigDecimal expectedTroco = received.subtract(total).setScale(2, RoundingMode.HALF_UP);

        if (troco.compareTo(expectedTroco) != 0) {
            throw new BadRequestException("Valor do troco inserido inválido.\nTotal: " + total
                    + "\nValor Recebido: " + received + "\nTroco inserido: " + troco);
        }
    }
}
