package com.rssecurity.storemanager.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioDTO(
        Long idUsuario,
        @NotBlank(message = "username deve ser fornecido")
        @Size(min = 4, max = 20, message = "deve ter entre 4 e 20 caracteres")
        String username,
        @Size(min = 8, max = 24, message = "deve ter entre 8 e 24 caracteres")
        String senha,
        @NotBlank(message = "nome não pode estar em branco")
        @Size(min = 2, max = 70, message = "deve ter entre 2 e 70 caracteres")
        String nome,
        @NotBlank(message = "sobrenome não pode estar em branco")
        @Size(min = 2, max = 100, message = "deve ter entre 2 e 100 caracteres")
        String sobrenome,
        @NotBlank(message = "cpf não pode estar em branco")
        @Size(min = 11, max = 11, message = "deve ter 11 caracteres numéricos")
        @Pattern(regexp = "^\\d+$", message = "deve conter apenas números")
        String cpf,
        @NotBlank(message = "não pode estar em branco")
        @Size(min = 3, max = 255, message = "deve conter entre 3 e 255 caracteres")
        String endereco,
        @NotBlank(message = "endereço não pode estar em branco")
        @Size(min = 4, max = 255, message = "email deve conter entre 4 e 255 caracteres")
        @Email(message = "email inválido")
        String email,
        @NotBlank(message = "não pode estar em branco")
        @Size(min = 10, max = 11, message = "deve conter 10 ou 11 caracteres")
        @Pattern(regexp = "^\\d+$", message = "deve conter apenas números")
        String telefone,
        BigDecimal salario,
        @NotNull(message = "permissões devem ser definidas")
        Boolean admin
) {
}
