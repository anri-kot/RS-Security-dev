package com.rssecurity.storemanager.service;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.exception.BadRequestException;
import com.rssecurity.storemanager.mapper.ProdutoMapper;
import com.rssecurity.storemanager.model.Produto;
import com.rssecurity.storemanager.repository.ProdutoRepository;
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
        ProdutoDTO input = new ProdutoDTO(null, "CAMERA SPX", new BigDecimal("50.00"), "Camera de alta definicao", 30, inCategoria);
        Produto entity = new Produto(); // preenche se quiser
        Produto saved = new Produto();  // pode simular que foi salvo

        CategoriaDTO outCategoria = new CategoriaDTO(2L, "Vigilancia");
        ProdutoDTO output = new ProdutoDTO(1L, "CAMERA SPX", new BigDecimal("50.00"), "Camera de alta definicao", 30, inCategoria);

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
        ProdutoDTO input = new ProdutoDTO(22L, "Camera", new BigDecimal("50.00"), "Camera de alta definicao", 30, inCategoria);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> service.create(input));

        assertEquals("Campo ID não deve ser fornecido ou deve ser nulo.", ex.getMessage());
        verifyNoInteractions(repository);
    }
}