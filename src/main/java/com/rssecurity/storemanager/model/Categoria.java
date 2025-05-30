package com.rssecurity.storemanager.model;

import jakarta.persistence.*;

@Entity
@Table(name="categoria", uniqueConstraints = {
        @UniqueConstraint(columnNames = "nome")
})
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
