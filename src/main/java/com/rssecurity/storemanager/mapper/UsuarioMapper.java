package com.rssecurity.storemanager.mapper;

import com.rssecurity.storemanager.dto.UsuarioDTO;
import com.rssecurity.storemanager.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);
    @Mapping(source = "admin", target = "isAdmin")
    UsuarioDTO toDTO(Usuario usuario);
    @Mapping(source = "isAdmin", target = "admin")
    Usuario toEntity(UsuarioDTO dto);
}
