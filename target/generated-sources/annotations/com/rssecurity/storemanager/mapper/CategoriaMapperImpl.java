package com.rssecurity.storemanager.mapper;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.model.Categoria;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-14T21:40:57-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Arch Linux)"
)
@Component
public class CategoriaMapperImpl implements CategoriaMapper {

    @Override
    public CategoriaDTO toDTO(Categoria categoria) {
        if ( categoria == null ) {
            return null;
        }

        String nome = null;

        nome = categoria.getNome();

        Long id = null;

        CategoriaDTO categoriaDTO = new CategoriaDTO( id, nome );

        return categoriaDTO;
    }

    @Override
    public Categoria toEntity(CategoriaDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Categoria categoria = new Categoria();

        categoria.setNome( dto.nome() );

        return categoria;
    }
}
