package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UsuarioDTO(
        Long idUsuario,
        @NotBlank(message = "username deve ser fornecido")
        String username,
        String senha,
        @NotBlank(message = "nome não pode estar em branco")
        String nome,
        @NotBlank(message = "sobrenome não pode estar em branco")
        String sobrenome,
        @NotBlank(message = "cpf não pode estar em branco")
        String cpf,
        String endereco,
        @NotBlank(message = "endereço não pode estar em branco")
        String email,
        String telefone,
        BigDecimal salario,
        @NotNull(message = "permissões devem ser definidas")
        Boolean admin
) {
}
