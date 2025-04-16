package com.rssecurity.storemanager.mapper;

import com.rssecurity.storemanager.dto.CategoriaDTO;
import com.rssecurity.storemanager.model.Categoria;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-15T21:24:34-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class CategoriaMapperImpl implements CategoriaMapper {

    @Override
    public CategoriaDTO toDTO(Categoria categoria) {
        if ( categoria == null ) {
            return null;
        }

        Long idCategoria = null;
        String nome = null;

        idCategoria = categoria.getIdCategoria();
        nome = categoria.getNome();

        CategoriaDTO categoriaDTO = new CategoriaDTO( idCategoria, nome );

        return categoriaDTO;
    }

    @Override
    public Categoria toEntity(CategoriaDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Categoria categoria = new Categoria();

        categoria.setIdCategoria( dto.idCategoria() );
        categoria.setNome( dto.nome() );

        return categoria;
    }
}
