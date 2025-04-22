package com.rssecurity.storemanager.mapper;

import com.rssecurity.storemanager.dto.VendaDTO;
import com.rssecurity.storemanager.model.Venda;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VendaMapper {
    VendaMapper INSTANCE = Mappers.getMapper(VendaMapper.class);
    VendaDTO toDTO(Venda venda);
    Venda toEntity(VendaDTO toDTO);
}
