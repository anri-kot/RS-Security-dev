package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.categoria.dto.CategoriaDTO;
import com.rssecurity.storemanager.infra.exception.BadRequestException;
import com.rssecurity.storemanager.produto.dto.ProdutoDTO;
import com.rssecurity.storemanager.produto.mapper.ProdutoMapper;
import com.rssecurity.storemanager.produto.model.Produto;
import com.rssecurity.storemanager.produto.repository.ProdutoRepository;
import com.rssecurity.storemanager.produto.service.ProdutoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {
    @InjectMocks
    private ProdutoService service;

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ProdutoMapper mapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarProdutoQuandoIdForNulo() {
        CategoriaDTO inCategoria = new CategoriaDTO(2L, "");
        ProdutoDTO input = new ProdutoDTO(null, "CAMERA SPX", null, new BigDecimal("50.00"), "Camera de alta definicao", 30, inCategoria);
        Produto entity = new Produto(); // preenche se quiser
        Produto saved = new Produto();  // pode simular que foi salvo

        ProdutoDTO output = new ProdutoDTO(1L, "CAMERA SPX", null, new BigDecimal("50.00"), "Camera de alta definicao", 30, inCategoria);

        when(mapper.toEntity(input)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(output);

        ProdutoDTO result = service.create(input);

        assertEquals(output, result);
        verify(repository).save(entity);
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoForNulo() {
        CategoriaDTO inCategoria = new CategoriaDTO(2L, "");
        ProdutoDTO input = new ProdutoDTO(22L, "Camera", null, new BigDecimal("50.00"), "Camera de alta definicao", 30, inCategoria);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> service.create(input));

        assertEquals("Campo ID não deve ser fornecido ou deve ser nulo.", ex.getMessage());
        verifyNoInteractions(repository);
    }
}