package com.rssecurity.storemanager.repository;

import com.rssecurity.storemanager.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByFornecedor_IdFornecedor(Long idFornecedor);
    List<Compra> findByDataBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Compra> findByDataAfter(LocalDateTime data);
    List<Compra> findByDataBefore(LocalDateTime data);
    List<Compra> findByFornecedor_NomeContaining(String nome);
    List<Compra> findByObservacaoContaining(String observacao);
}
