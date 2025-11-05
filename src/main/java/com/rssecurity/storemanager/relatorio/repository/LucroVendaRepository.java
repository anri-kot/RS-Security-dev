package com.rssecurity.storemanager.relatorio.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rssecurity.storemanager.relatorio.model.LucroVenda;

@Repository
public interface LucroVendaRepository extends JpaRepository<LucroVenda, Long> {
    List<LucroVenda> findByDataBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
        SELECT SUM(lv.lucroTotal)
        FROM LucroVenda lv
        WHERE lv.data BETWEEN :start AND :end
    """)
    BigDecimal calcularLucroTotalBetween(@Param("start") LocalDateTime dataInicio, @Param("end") LocalDateTime dataFim);

    @Query("""
        SELECT SUM(lv.custoTotal)
        FROM LucroVenda lv
        WHERE lv.data BETWEEN :start AND :end
    """)
    BigDecimal calcularCustoMedioTotalBetween(@Param("start") LocalDateTime dataInicio, @Param("end") LocalDateTime dataFim);
}
