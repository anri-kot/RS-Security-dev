package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioResumoDTO(
        Long idUsuario,
        @NotBlank
        String username,
        @NotBlank
        String nome,
        @NotBlank
        String sobrenome
) {
}
