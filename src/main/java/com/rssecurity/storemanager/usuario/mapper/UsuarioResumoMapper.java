package com.rssecurity.storemanager.usuario.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rssecurity.storemanager.usuario.dto.UsuarioResumoDTO;
import com.rssecurity.storemanager.usuario.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioResumoMapper {
    UsuarioResumoDTO toDTO(Usuario usuario);

    @Mapping(target = "admin", ignore = true)
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    @Mapping(target = "salario", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "telefone", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    Usuario toEntity(UsuarioResumoDTO dto);
}
