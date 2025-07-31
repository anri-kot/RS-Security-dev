package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FornecedorDTO(Long idFornecedor, @NotBlank(message = "nome não pode ser nulo") String nome, @NotBlank(message = "cnpj não pode estar em branco") String cnpj, @NotBlank(message = "telefone não pode estar em branco") String telefone, @NotBlank(message = "email não pode estar em branco") String email) {}
