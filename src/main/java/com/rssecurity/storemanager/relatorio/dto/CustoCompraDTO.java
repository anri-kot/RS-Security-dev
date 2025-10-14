package com.rssecurity.storemanager.relatorio.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustoCompraDTO(
    Long idCompra,
    LocalDateTime data,
    BigDecimal custoTotal
) {}
