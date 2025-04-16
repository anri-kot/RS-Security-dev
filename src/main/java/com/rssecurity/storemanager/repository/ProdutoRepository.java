package com.rssecurity.storemanager.repository;

import com.rssecurity.storemanager.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContains(String nome);
    List<Produto> findByDescricaoContains(String descricao);

}
