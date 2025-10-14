package com.rssecurity.storemanager.relatorio.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rssecurity.storemanager.relatorio.model.CustoCompra;

@Repository
public interface CustoCompraRepository extends JpaRepository<CustoCompra, Long> {
    @Query("""
        SELECT SUM(cc.custoTotal)
        FROM CustoCompra cc
        WHERE cc.data BETWEEN :start AND :end
    """)
    BigDecimal calcularCustoTotalBetween(@Param("start") LocalDateTime dataInicio, @Param("end") LocalDateTime dataFim);
}
