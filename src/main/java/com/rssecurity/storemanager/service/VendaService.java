package com.rssecurity.storemanager.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final ProdutoMapper produtoMapper;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final ProdutoService produtoService;

    public VendaService(VendaRepository repository, VendaMapper mapper, ProdutoMapper produtoMapper,
            UsuarioRepository usuarioRepository, ProdutoRepository produtoRepository, ProdutoService produtoService) {
        this.repository = repository;
        this.mapper = mapper;
        this.produtoMapper = produtoMapper;
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
        this.produtoService = produtoService;
    }

    // SEARCH

    public List<VendaDTO> findAll() {
        return repository.findAll(SORT_BY_DATE).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<VendaDTO> findAllByCustomMatcher(Map<String, String> filter) {
        filter.values().removeIf(String::isBlank);

        Specification<Venda> spec = VendaSpecification.withFilters(filter);
        return repository.findAll(spec, SORT_BY_DATE).stream()
                .map(mapper::toDTO)
                .toList();
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

    // ACTIONS

    // Overloaded method for PDV transactions
    public VendaDTO create(VendaDTO venda, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado. Username: " + username));
        UsuarioResumoDTO usu = new UsuarioResumoDTO(usuario.getIdUsuario(), usuario.getUsername(), usuario.getNome(),
                usuario.getSobrenome());

        BigDecimal troco = null;
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
        List<ItemVenda> itens = getItens(venda, entity, false);

        entity.setItens(itens);
        VendaDTO saved = mapper.toDTO(repository.save(entity));

        updateStock(venda);

        return saved;
    }

    @Transactional
    public void update(Long idVenda, VendaDTO venda) {
        VendaDTO oldVenda = findById(idVenda);

        validateUsuario(venda.usuario());
        if (venda.metodoPagamento().equals("DINHEIRO")) {
            validateTroco(venda);
        }

        Venda entity = mapper.toEntity(venda);
        List<ItemVenda> itens = getItens(venda, entity, true);
        entity.setItens(itens);

        VendaDTO saved = mapper.toDTO(repository.save(entity));

        updateStock(oldVenda, saved);
    }

    public void deleteById(Long idVenda) {
        if (!repository.existsById(idVenda)) {
            throw new ResourceNotFoundException("Venda não encontrada. ID: " + idVenda);
        }

        repository.deleteById(idVenda);
    }

    private void updateStock(VendaDTO newVenda) {
        updateStock(null, newVenda);
    }

    private void updateStock(VendaDTO oldVenda, VendaDTO newVenda) {
        Map<Long, Integer> oldQuantidades = new HashMap<>();

        // Old quantities (stock reposition, hence the negative value)
        if (oldVenda != null) {
            for (ItemVendaDTO item : oldVenda.itens()) {
                long produtoId = item.produto().idProduto();
                oldQuantidades.put(produtoId, item.quantidade());
            }
        }

        for (ItemVendaDTO item : newVenda.itens()) {
            long produtoId = item.produto().idProduto();
            int oldQty = oldQuantidades.getOrDefault(produtoId, 0);
            int newQty = item.quantidade();
            int stockDifference = oldQty - newQty; // positive value = refill stock, negative = remove from stock

            Produto produto = produtoMapper.toEntity(item.produto());
            produto.setEstoque(produto.getEstoque() + stockDifference);

            produtoService.update(produtoId, produtoMapper.toDTO(produto));
        }
    }

    private List<ItemVenda> getItens(VendaDTO venda, Venda entity, boolean isUpdate) {
        List<Long> idProdutos = new ArrayList<>();
        List<ItemVenda> itens = venda.itens().stream()
                .map(itemDto -> {
                    ItemVenda item = new ItemVenda();
                    if (isUpdate) {
                        item.setIdItem(itemDto.idItem());
                    }
                    item.setProduto(produtoMapper.toEntity(itemDto.produto()));
                    item.setQuantidade(itemDto.quantidade());
                    item.setValorUnitario(itemDto.valorUnitario());
                    item.setDesconto(itemDto.desconto());
                    item.setVenda(entity);
                    idProdutos.add(item.getProduto().getIdProduto());
                    return item;
                }).toList();
        validateProduto(itens);
        return itens;
    }

    private void validateUsuario(UsuarioResumoDTO usuario) {
        Usuario theUsuario = usuarioRepository.findById(usuario.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado. ID: " + usuario.idUsuario()));

        // Compares the inputed username with the register in the database
        if (!theUsuario.getUsername().equals(usuario.username())) {
            throw new ConflictException(
                    "Username inserido não corresponde ao ID do usuario no banco de dados. ID e Usuario informados: "
                            + usuario.idUsuario() + " | " + usuario.username());
        }
    }

    // TODO: Assign MIN_STOCK to user preferences
    private void validateProduto(List<ItemVenda> itens) {
        final int MIN_STOCK = 1;

        for (ItemVenda item : itens) {
            if (!produtoRepository.existsById(item.getProduto().getIdProduto())) {
                throw new ResourceNotFoundException("Produto não encontrado. ID: " + item.getProduto().getIdProduto());
            }

            if ((item.getProduto().getEstoque() - item.getQuantidade()) < MIN_STOCK) {
                throw new BadRequestException("Estoque insuficiente para o produto: " + item.getProduto().getNome());
            }
        }
    }

    private void validateTroco(VendaDTO venda) {
        BigDecimal total = venda.getTotal();
        BigDecimal change = venda.valorRecebido().subtract(total);
        if (venda.troco().compareTo(change) != 0) {
            throw new BadRequestException("Valor do troco inserido inválido.\nTotal: " + total + "\nValor Recebido: "
                    + venda.valorRecebido() + "\nTroco inserido: " + venda.troco());
        }
    }
}
