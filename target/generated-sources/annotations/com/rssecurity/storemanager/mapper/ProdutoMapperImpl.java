package com.rssecurity.storemanager.mapper;

import com.rssecurity.storemanager.dto.ProdutoDTO;
import com.rssecurity.storemanager.model.Categoria;
import com.rssecurity.storemanager.model.Produto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-15T21:27:34-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Arch Linux)"
)
@Component
public class ProdutoMapperImpl implements ProdutoMapper {

    @Override
    public ProdutoDTO toDTO(Produto produto) {
        if ( produto == null ) {
            return null;
        }

        Long idCategoria = null;
        Long idProduto = null;
        String nome = null;
        String descricao = null;
        Integer estoqueMin = null;

        idCategoria = produtoCategoriaIdCategoria( produto );
        if ( produto.getIdProduto() != null ) {
            idProduto = produto.getIdProduto().longValue();
        }
        nome = produto.getNome();
        descricao = produto.getDescricao();
        estoqueMin = produto.getEstoqueMin();

        ProdutoDTO produtoDTO = new ProdutoDTO( idProduto, nome, descricao, estoqueMin, idCategoria );

        return produtoDTO;
    }

    @Override
    public Produto toEntity(ProdutoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Produto produto = new Produto();

        produto.setCategoria( produtoDTOToCategoria( dto ) );
        if ( dto.idProduto() != null ) {
            produto.setIdProduto( dto.idProduto().intValue() );
        }
        produto.setNome( dto.nome() );
        produto.setDescricao( dto.descricao() );
        produto.setEstoqueMin( dto.estoqueMin() );

        return produto;
    }

    private Long produtoCategoriaIdCategoria(Produto produto) {
        if ( produto == null ) {
            return null;
        }
        Categoria categoria = produto.getCategoria();
        if ( categoria == null ) {
            return null;
        }
        Long idCategoria = categoria.getIdCategoria();
        if ( idCategoria == null ) {
            return null;
        }
        return idCategoria;
    }

    protected Categoria produtoDTOToCategoria(ProdutoDTO produtoDTO) {
        if ( produtoDTO == null ) {
            return null;
        }

        Categoria categoria = new Categoria();

        categoria.setIdCategoria( produtoDTO.idCategoria() );

        return categoria;
    }
}
