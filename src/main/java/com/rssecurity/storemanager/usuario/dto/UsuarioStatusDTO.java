package com.rssecurity.storemanager.usuario.dto;

import jakarta.validation.constraints.NotNull;

public record UsuarioStatusDTO(@NotNull(message = "Status do usuário não definido.") Boolean status) {}
