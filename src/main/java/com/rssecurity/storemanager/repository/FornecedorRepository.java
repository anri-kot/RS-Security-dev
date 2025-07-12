package com.rssecurity.storemanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rssecurity.storemanager.model.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    List<Fornecedor> findByNomeContains(String name);
    List<Fornecedor> findByCnpjContains(String cnpj);
    List<Fornecedor> findByTelefoneContains(String telefone);
    List<Fornecedor> findByEmailContains(String email);
    Optional<Fornecedor> findByCnpj(String cnpj);
    Optional<Fornecedor> findByTelefone(String telefone);
    Optional<Fornecedor> findByEmail(String email);

    // Pages

    Page<Fornecedor> findByNomeContains(String nome, Pageable p);
    Page<Fornecedor> findByCnpjContains(String cnpj, Pageable p);
    Page<Fornecedor> findByTelefoneContains(String telefone, Pageable p);
    Page<Fornecedor> findByEmailContains(String email, Pageable p);

    boolean existsByCnpj(String cnpj);
    boolean existsByTelefone(String telefone);
    boolean existsByEmail(String email);
}
