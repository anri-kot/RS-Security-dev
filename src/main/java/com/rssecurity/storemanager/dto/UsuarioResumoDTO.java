package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioResumoDTO(
        Long idUsuario,
        @NotBlank(message = "username não pode estar em branco")
        String username,
        @NotBlank(message = "nome não pode estar em branco")
        String nome,
        @NotBlank
        @NotBlank(message = "sobrenome não pode estar em branco")
        String sobrenome
) {
}
