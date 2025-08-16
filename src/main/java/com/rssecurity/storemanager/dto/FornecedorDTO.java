package com.rssecurity.storemanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FornecedorDTO(
    Long idFornecedor,
    @NotBlank(message = "nome não pode ser nulo")
    @Size(min = 3, max = 255, message = "deve conter entre 3 e 255 caracteres")
    String nome,
    @NotBlank(message = "cnpj não pode estar em branco")
    @Pattern(regexp = "^\\d+$", message = "deve conter apenas números")
    @Size(max = 14, message = "deve conter no máximo 14 digitos")
    String cnpj,
    @NotBlank(message = "telefone não pode estar em branco")
    @Pattern(regexp = "^\\d+$", message = "deve conter apenas números")
    String telefone,
    @NotBlank(message = "email não pode estar em branco")
    @Email(message = "deve ser um email válido")
    String email
    ) {}
