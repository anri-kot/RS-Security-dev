package com.rssecurity.storemanager.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rssecurity.storemanager.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long>, JpaSpecificationExecutor<Compra> {
    List<Compra> findByFornecedor_IdFornecedor(Long idFornecedor);
    List<Compra> findByDataBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Compra> findByDataAfter(LocalDateTime data);
    List<Compra> findByDataBefore(LocalDateTime data);
    List<Compra> findByFornecedor_NomeContaining(String nome);
    List<Compra> findByObservacaoContaining(String observacao);

    // Reports
    @Query("""
        SELECT COALESCE(SUM(i.valorUnitario * i.quantidade), 0)
        FROM ItemCompra i
        WHERE i.compra.data BETWEEN :start AND :end
    """)
    BigDecimal calculateTotalCompraValueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
