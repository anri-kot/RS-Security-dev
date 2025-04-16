package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotNull;;

public record FornecedorDTO(Long idFornecedor, @NotNull String nome, @NotNull String cnpj, @NotNull String telefone, @NotNull String email) {}
