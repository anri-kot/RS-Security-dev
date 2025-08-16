package com.rssecurity.storemanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// import com.rssecurity.storemanager.dto.ProdutoEstoqueDTO;
import com.rssecurity.storemanager.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContains(String nome);
    Optional<Produto> findByCodigoBarras(String codigoBarras);
    List<Produto> findByDescricaoContains(String descricao);
    List<Produto> findByCategoria_Nome(String categoria);
    List<Produto> findByCodigoBarrasContains(String codigoBarras);

    List<Produto> findByNomeContainsIgnoreCaseAndCategoria_IdCategoria(String nome, Long categoria);

    Page<Produto> findByNomeContains(String nome, Pageable p);
    Page<Produto> findByDescricaoContains(String descricao, Pageable p);
    Page<Produto> findByCategoria_Nome(String categoria, Pageable p);
    Page<Produto> findByCodigoBarrasContains(String codigoBarras, Pageable p);

    Page<Produto> findByNomeContainsIgnoreCaseAndCategoria_IdCategoria(String nome, Long categoria, Pageable p);

    boolean existsByCodigoBarras(String codigoBarras);
}
