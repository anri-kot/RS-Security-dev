package com.rssecurity.storemanager.compra.dto;

import java.time.LocalDateTime;

public record CompraKey(LocalDateTime data, String observacao, Long idFornecedor, String nomeFornecedor) {}
