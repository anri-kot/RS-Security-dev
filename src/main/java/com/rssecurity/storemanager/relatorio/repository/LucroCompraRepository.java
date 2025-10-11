package com.rssecurity.storemanager.relatorio.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rssecurity.storemanager.relatorio.model.LucroCompra;

@Repository
public interface LucroCompraRepository extends JpaRepository<LucroCompra, Long> {
    @Query("""
        SELECT SUM(lc.custoTotal)
        FROM LucroCompra lc
        WHERE lc.data BETWEEN :start AND :end
    """)
    BigDecimal calcularCustoTotal(@Param("start") LocalDateTime dataInicio, @Param("end") LocalDateTime dataFim);
}
