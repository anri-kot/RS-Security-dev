package com.rssecurity.storemanager.repository;

import com.rssecurity.storemanager.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    List<Fornecedor> findByNomeContains(String name);
    Optional<Fornecedor> findByCnpj(String cnpj);
    Optional<Fornecedor> findByTelefone(String telefone);
    Optional<Fornecedor> findByEmail(String email);

    boolean existsByCnpj(String cnpj);
    boolean existsByTelefone(String telefone);
    boolean existsByEmail(String email);
}
