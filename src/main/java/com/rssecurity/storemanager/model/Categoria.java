package com.rssecurity.storemanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Categoria {
    @Id
    private Long idCategoria;
    private String nome;

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
