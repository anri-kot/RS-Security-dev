package com.rssecurity.storemanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.rssecurity.storemanager.produto.controller.ProdutoController;
import com.rssecurity.storemanager.produto.service.ProdutoService;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;


}