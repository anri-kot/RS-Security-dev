package com.rssecurity.storemanager.service;

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
import com.rssecurity.storemanager.repository.ItemCompraRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompraService {
    private CompraRepository repository;
    private ItemCompraRepository itemRepository;
    private CompraMapper mapper;
    private ProdutoMapper produtoMapper;

    public CompraService(CompraRepository repository, ItemCompraRepository itemRepository, CompraMapper mapper, ProdutoMapper produtoMapper) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.mapper = mapper;
        this.produtoMapper = produtoMapper;
    }

    public List<CompraDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public CompraDTO findById(Long idCompra) {
        Compra compra = repository.findById(idCompra)
                .orElseThrow(() -> new ResourceNotFoundException("Compra não encontrada. ID: " + idCompra));
        return mapper.toDTO(compra);
    }

    public CompraDTO create(@RequestBody CompraDTO dto) {
        if (dto.idCompra() != null) {
            throw new BadRequestException("Campo ID não deve ser fornecido ou deve ser nulo.");
        }

        Compra entity = mapper.toEntity(dto);

        List<ItemCompra> itens = dto.itens().stream()
                .map(itemDto -> {
                    ItemCompra item = new ItemCompra();
                    item.setidItem(itemDto.idItem());
                    item.setQuantidade(itemDto.quantidade());
                    item.setValorUnitario(itemDto.valorUnitario());
                    item.setProduto(produtoMapper.toEntity(itemDto.produto()));
                    item.setCompra(entity);
                    return item;
                }).toList();

        entity.setItens(itens);
        Compra saved = repository.save(entity);

        return mapper.toDTO(saved);
    }

}
