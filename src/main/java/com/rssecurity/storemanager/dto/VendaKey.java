package com.rssecurity.storemanager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VendaKey(LocalDateTime data, String observacao, String username, BigDecimal valorRecebido, String metodoPagamento, BigDecimal troco) {
    
}
