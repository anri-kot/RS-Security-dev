package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.dto.ProdutoEstoqueDTO;
import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.dto.UsuarioResumoDTO;
import com.rssecurity.storemanager.exception.BadRequestException;
import com.rssecurity.storemanager.exception.ConflictException;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.mapper.ProdutoMapper;
import com.rssecurity.storemanager.mapper.VendaMapper;
import com.rssecurity.storemanager.model.ItemVenda;
import com.rssecurity.storemanager.model.Produto;
import com.rssecurity.storemanager.model.Usuario;
import com.rssecurity.storemanager.model.Venda;
import com.rssecurity.storemanager.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VendaService {
    private final VendaRepository repository;
    private final VendaMapper mapper;
    private final ProdutoMapper produtoMapper;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService(VendaRepository repository, VendaMapper mapper, ProdutoMapper produtoMapper, UsuarioRepository usuarioRepository, ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.produtoMapper = produtoMapper;
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
    }

    // SEARCH

    public List<VendaDTO> findAll() {
        return repository.findAll().stream()
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

    public VendaDTO create(VendaDTO venda) {
        if (venda.idVenda() != null) {
            throw new BadRequestException("Campo ID não deve ser fornecido ou deve ser nulo.");
        }
        validateUsuario(venda.usuario());

        Venda entity = mapper.toEntity(venda);
        List<ItemVenda> itens = getItens(venda, entity, false);

        entity.setItens(itens);
        Venda saved = repository.save(entity);

        return mapper.toDTO(saved);
    }

    public void update(Long idVenda, VendaDTO venda) {
        if (!repository.existsById(idVenda)) {
            throw new ResourceNotFoundException("Venda não encontrada. ID: " + idVenda);
        }
        validateUsuario(venda.usuario());

        Venda entity = mapper.toEntity(venda);
        List<ItemVenda> itens = getItens(venda, entity, true);
        entity.setItens(itens);

        repository.save(entity);
    }

    public void deleteById(Long idVenda) {
        if (!repository.existsById(idVenda)) {
            throw new ResourceNotFoundException("Venda não encontrada. ID: " + idVenda);
        }

        repository.deleteById(idVenda);
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
            throw new ConflictException("Username inserido não corresponde ao ID do usuario no banco de dados. ID e Usuario informados: "
                    + usuario.idUsuario() + " | " + usuario.username());
        }
    }

    private void validateProduto(List<ItemVenda> itens) {
        List<Long> ids = itens.stream()
                .map(item -> item.getProduto().getIdProduto())
                .toList();

        Map<Long, ProdutoEstoqueDTO> estoqueMap = produtoRepository.findEstoqueAtualByProdutoIds(ids).stream()
                .collect(Collectors.toMap(ProdutoEstoqueDTO::getIdProduto, Function.identity()));

        for (ItemVenda item : itens) {
            ProdutoEstoqueDTO dto = estoqueMap.get(item.getProduto().getIdProduto());
            if (dto == null) {
                throw new ResourceNotFoundException("Produto não encontrado. ID: " + item.getProduto().getIdProduto());
            }

            if (item.getQuantidade() > dto.getEstoque()) {
                throw new BadRequestException("Estoque insuficiente para o produto: " + item.getProduto().getNome());
            }
        }
    }
}
