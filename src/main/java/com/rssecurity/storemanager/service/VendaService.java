package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.dto.VendaDTO;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendaService {
    private VendaRepository repository;
    private VendaMapper mapper;
    private ProdutoMapper produtoMapper;
    private UsuarioRepository usuarioRepository;
    private ProdutoRepository produtoRepository;

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
        validadeProduto(idProdutos);
        return itens;
    }

    private void validateUsuario(UsuarioResumoDTO usuario) {
        Usuario theUsuario = usuarioRepository.findById(usuario.idUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado. ID: " + usuario.idUsuario()));

        // Compares the inputed username with the register in the database
        if (!theUsuario.getUsername().equals(usuario.idUsuario())) {
            throw new ConflictException("Username inserido não corresponde ao ID do usuario no banco de dados. ID e Usuario informados: "
                    + usuario.idUsuario() + " | " + usuario.username());
        }
    }

    private void validadeProduto(List<Long> ids) {
        List<Long> found = produtoRepository.findAllById(ids).stream()
                .map(Produto::getIdProduto)
                .toList();
        for (Long id : ids) {
            if (!found.contains(id)) {
                throw new ResourceNotFoundException("Produto não encontrado. ID: " + id);
            }
        }
    }
}
