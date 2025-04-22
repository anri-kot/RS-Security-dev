package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.dto.CompraDTO;
import com.rssecurity.storemanager.mapper.CompraMapper;
import com.rssecurity.storemanager.mapper.ProdutoMapper;
import com.rssecurity.storemanager.model.Compra;
import com.rssecurity.storemanager.repository.CompraRepository;
import com.rssecurity.storemanager.repository.ItemCompraRepository;
import com.rssecurity.storemanager.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompraServiceTest {

    private CompraRepository compraRepository;
    private ItemCompraRepository itemRepository;
    private CompraMapper compraMapper;
    private ProdutoMapper produtoMapper;
    private CompraService compraService;

    @BeforeEach
    void setUp() {
        compraRepository = mock(CompraRepository.class);
        itemRepository = mock(ItemCompraRepository.class);
        compraMapper = mock(CompraMapper.class);
        produtoMapper = mock(ProdutoMapper.class);
        compraService = new CompraService(compraRepository, itemRepository, compraMapper, produtoMapper);
    }

    @Test
    void deveRetornarTodasAsCompras() {
        Compra compra = new Compra();
        CompraDTO compraDTO = new CompraDTO(1L, LocalDateTime.now(), "obs", null, List.of());

        when(compraRepository.findAll()).thenReturn(List.of(compra));
        when(compraMapper.toDTO(compra)).thenReturn(compraDTO);

        List<CompraDTO> resultado = compraService.findAll();

        assertThat(resultado).hasSize(1);
        verify(compraRepository, times(1)).findAll();
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

