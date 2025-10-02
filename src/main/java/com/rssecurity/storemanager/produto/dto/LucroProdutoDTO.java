package com.rssecurity.storemanager.produto.dto;

import java.math.BigDecimal;

public record LucroProdutoDTO(
    Long idProduto,
    String nome,
    BigDecimal custo,
    BigDecimal receita,
    BigDecimal lucro
) {}
