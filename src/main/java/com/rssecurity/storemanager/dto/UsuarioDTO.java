package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UsuarioDTO(
        Long idUsuario,
        @NotBlank
        String username,
        @NotBlank
        String senha,
        @NotBlank
        String nome,
        @NotBlank
        String sobrenome,
        @NotBlank
        String cpf,
        String endereco,
        @NotBlank
        String email,
        String telefone,
        BigDecimal salario,
        @NotNull
        Boolean admin
) {
}
