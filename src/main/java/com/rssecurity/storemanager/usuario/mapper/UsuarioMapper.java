package com.rssecurity.storemanager.usuario.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.rssecurity.storemanager.usuario.dto.UsuarioDTO;
import com.rssecurity.storemanager.usuario.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);
    UsuarioDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioDTO dto);
}
