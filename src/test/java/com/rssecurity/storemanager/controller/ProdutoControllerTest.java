package com.rssecurity.storemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Test
    void deveRetornarCreatedQuandoProdutoValido() throws Exception {
        ProdutoDTO dto = new ProdutoDTO(null, "Produto Teste", "", 5, null);
        ProdutoDTO criado = new ProdutoDTO(1L, "Produto Teste", "", 5, null);

        when(produtoService.create(any())).thenReturn(criado);

        mockMvc.perform(post("/api/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProduto").value(1L));
    }
}