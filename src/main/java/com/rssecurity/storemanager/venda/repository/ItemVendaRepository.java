package com.rssecurity.storemanager.venda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rssecurity.storemanager.venda.model.ItemVenda;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {}
