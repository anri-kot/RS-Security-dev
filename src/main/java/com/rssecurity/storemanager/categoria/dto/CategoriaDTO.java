package com.rssecurity.storemanager.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaDTO(
    Long idCategoria,
    @NotBlank(message = "nome não pode estar em branco")
    @Size(min = 2, max = 255, message = "nome deve ter entre 2 e 255 caracteres")
    String nome
    ) {
}
