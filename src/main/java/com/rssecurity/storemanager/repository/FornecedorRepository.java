package com.rssecurity.storemanager.repository;

import com.rssecurity.storemanager.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    List<Fornecedor> findByName(String name);
    Fornecedor findByCnpj(String cnpj);
    Fornecedor findByTelefone(String telefone);
    Fornecedor findByEmail(String email);
}
