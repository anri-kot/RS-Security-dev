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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import com.rssecurity.storemanager.compra.dto.CompraDTO;
import com.rssecurity.storemanager.compra.mapper.CompraMapper;
import com.rssecurity.storemanager.compra.model.Compra;
import com.rssecurity.storemanager.compra.repository.CompraRepository;
import com.rssecurity.storemanager.compra.service.CompraService;
import com.rssecurity.storemanager.infra.exception.ResourceNotFoundException;
import com.rssecurity.storemanager.produto.mapper.ProdutoMapper;
import com.rssecurity.storemanager.produto.repository.ProdutoRepository;

class CompraServiceTest {

    private CompraRepository compraRepository;
    private CompraMapper compraMapper;
    private ProdutoMapper produtoMapper;
    private CompraService compraService;
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void setUp() {
        compraRepository = mock(CompraRepository.class);
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
