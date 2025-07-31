package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(Long idCategoria, @NotBlank(message = "nome não pode estar em branco") String nome) {
}
