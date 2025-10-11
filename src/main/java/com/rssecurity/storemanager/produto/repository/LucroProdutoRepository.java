package com.rssecurity.storemanager.produto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rssecurity.storemanager.relatorio.model.LucroCompra;

public interface LucroProdutoRepository extends JpaRepository<LucroCompra, Long> {}
