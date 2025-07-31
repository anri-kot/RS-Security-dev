package com.rssecurity.storemanager.repository;

import com.rssecurity.storemanager.model.Categoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findAByNome(String nome);
    List<Categoria> findByNomeContains(String nome);

    Page<Categoria> findByNomeContains(String nome, Pageable p);
}
