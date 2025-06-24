package com.rssecurity.storemanager.repository;

// import com.rssecurity.storemanager.dto.ProdutoEstoqueDTO;
import com.rssecurity.storemanager.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContains(String nome);
    List<Produto> findByDescricaoContains(String descricao);
    List<Produto> findByCategoria_Nome(String categoria);

    List<Produto> findByNomeContainsIgnoreCaseAndCategoria_IdCategoria(String nome, Long categoria);

    // @Query("""
    // SELECT 
    //     p.idProduto AS idProduto,
    //     COALESCE(SUM(ic.quantidade), 0) - COALESCE(SUM(iv.quantidade), 0) AS estoque
    // FROM Produto p
    // LEFT JOIN ItemCompra ic ON ic.produto = p
    // LEFT JOIN ItemVenda iv ON iv.produto = p
    // WHERE p.idProduto IN :ids
    // GROUP BY p.idProduto
    // """)
    // List<ProdutoEstoqueDTO> findEstoqueAtualByProdutoIds(@Param("ids") List<Long> ids);

}
