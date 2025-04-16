package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotNull;

public record CategoriaDTO(Long idCategoria, @NotNull String nome) {
}
