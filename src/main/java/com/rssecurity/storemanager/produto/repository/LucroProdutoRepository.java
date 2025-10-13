package com.rssecurity.storemanager.produto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rssecurity.storemanager.relatorio.model.CustoCompra;

public interface LucroProdutoRepository extends JpaRepository<CustoCompra, Long> {}
