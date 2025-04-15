package com.rssecurity.storemanager.repository;

import com.rssecurity.storemanager.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findAByNome(String nome);
    List<Categoria> findByNomeContains(String nome);
}
