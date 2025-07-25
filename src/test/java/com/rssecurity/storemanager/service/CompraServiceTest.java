package com.rssecurity.storemanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rssecurity.storemanager.dto.CompraDTO;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.mapper.CompraMapper;
import com.rssecurity.storemanager.mapper.ProdutoMapper;
import com.rssecurity.storemanager.model.Compra;
import com.rssecurity.storemanager.repository.CompraRepository;
import com.rssecurity.storemanager.repository.ItemCompraRepository;
import com.rssecurity.storemanager.repository.ProdutoRepository;

class CompraServiceTest {

    private CompraRepository compraRepository;
    private ItemCompraRepository itemRepository;
    private CompraMapper compraMapper;
    private ProdutoMapper produtoMapper;
    private CompraService compraService;
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void setUp() {
        compraRepository = mock(CompraRepository.class);
        itemRepository = mock(ItemCompraRepository.class);
        compraMapper = mock(CompraMapper.class);
        produtoMapper = mock(ProdutoMapper.class);
        produtoRepository = mock(ProdutoRepository.class);
        compraService = new CompraService(compraRepository, compraMapper, produtoMapper, produtoRepository);
    }

    @Test
    void deveRetornarTodasAsCompras() {
        Compra compra = new Compra();
        compra.setIdCompra(1L);
        compra.setObservacao("obs");
        compra.setData(LocalDateTime.now());

        CompraDTO compraDTO = new CompraDTO(
                compra.getIdCompra(),
                compra.getData(),
                compra.getObservacao(),
                null,
                List.of());

        when(compraRepository.findAll(any(Sort.class))).thenReturn(List.of(compra));
        when(compraMapper.toDTO(any(Compra.class))).thenReturn(compraDTO);

        List<CompraDTO> resultado = compraService.findAll();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).observacao()).isEqualTo("obs");

        verify(compraRepository, times(1)).findAll(any(Sort.class));
        verify(compraMapper, times(1)).toDTO(any(Compra.class));
    }

    @Test
    void deveBuscarCompraPorId() {
        Long id = 1L;
        Compra compra = new Compra();
        CompraDTO dto = new CompraDTO(id, LocalDateTime.now(), "obs", null, List.of());

        when(compraRepository.findById(id)).thenReturn(Optional.of(compra));
        when(compraMapper.toDTO(compra)).thenReturn(dto);

        CompraDTO resultado = compraService.findById(id);

        assertThat(resultado.idCompra()).isEqualTo(id);
    }

    @Test
    void deveLancarExcecao_QuandoIdNaoEncontrado() {
        Long id = 1L;
        when(compraRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> compraService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Compra não encontrada");
    }
}
