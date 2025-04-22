package com.rssecurity.storemanager.repository;

import com.rssecurity.storemanager.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByUsuario_IdUsuario(Long idUsuario);
    List<Venda> findByUsuario_Username(String username);
    List<Venda> findByUsuario_NomeContainingOrUsuario_SobrenomeContaining(String nome, String sobrenome);

    List<Venda> findByDataBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Venda> findByDataAfter(LocalDateTime data);
    List<Venda> findByDataBefore(LocalDateTime data);

    List<Venda> findByObservacaoContaining(String observacao);
}