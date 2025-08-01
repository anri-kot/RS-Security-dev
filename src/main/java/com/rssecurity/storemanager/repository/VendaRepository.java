package com.rssecurity.storemanager.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rssecurity.storemanager.model.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long>, JpaSpecificationExecutor<Venda> {
    List<Venda> findByUsuario_IdUsuario(Long idUsuario);
    List<Venda> findByUsuario_Username(String username);
    List<Venda> findByUsuario_NomeContainingOrUsuario_SobrenomeContaining(String nome, String sobrenome);

    List<Venda> findByDataBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Venda> findByDataAfter(LocalDateTime data);
    List<Venda> findByDataBefore(LocalDateTime data);

    List<Venda> findByObservacaoContaining(String observacao);
    List<Venda> findByMetodoPagamento(String metodoPagamento);

    // Reports
    @Query("""
        SELECT COALESCE(SUM(v.valorRecebido - v.troco), 0)
        FROM Venda v
        WHERE v.data BETWEEN :start AND :end
    """)
    BigDecimal calculateTotalVendaValueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}