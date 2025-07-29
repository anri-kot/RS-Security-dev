package com.rssecurity.storemanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.rssecurity.storemanager.model.Venda;

@DataJpaTest
@ActiveProfiles("test")
public class VendaRepositoryTest {

    @Autowired
    private VendaRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnTotalVendasByDay() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay().minusNanos(1);
        
        Venda v1 = new Venda();
        v1.setData(LocalDateTime.of(today, LocalTime.of(10, 0)));
        v1.setObservacao("v1");
        v1.setValorRecebido(new BigDecimal("100"));
        v1.setTroco(new BigDecimal("0"));
        em.persist(v1);

        Venda v2 = new Venda();
        v2.setData(LocalDateTime.of(today, LocalTime.of(14, 30)));
        v2.setObservacao("v2");
        v2.setValorRecebido(new BigDecimal("250"));
        v2.setTroco(new BigDecimal("0"));
        em.persist(v2);

        Venda outroDia = new Venda();
        outroDia.setData(LocalDateTime.of(today.minusDays(1), LocalTime.of(15, 0)));
        outroDia.setObservacao("outro");
        outroDia.setValorRecebido(new BigDecimal("999"));
        outroDia.setTroco(new BigDecimal("0"));
        em.persist(outroDia);

        em.flush();

        List<Venda> vendas = repository.findAll();

        BigDecimal total = repository.calculateTotalVendaValueBetween(start, end);
        assertEquals(0, total.compareTo(new BigDecimal("350")));
    }

    @Test
    void shouldReturnZeroWhenNoSalesInPeriod() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 12, 31, 23, 59);

        BigDecimal result = repository.calculateTotalVendaValueBetween(start, end);

        assertEquals(BigDecimal.ZERO, result);
    }
}