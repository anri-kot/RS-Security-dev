package com.rssecurity.storemanager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VendaKey(LocalDateTime data, String observacao, String username, String metodoPagamento, BigDecimal troco, BigDecimal valorRecebido, BigDecimal desconto) {
    
}
