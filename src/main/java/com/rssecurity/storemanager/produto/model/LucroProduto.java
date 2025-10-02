package com.rssecurity.storemanager.produto.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vw_lucro_produto")
public class LucroProduto {
    @Id
    private Long idProduto;
    private String nome;
    private BigDecimal receita;
    private BigDecimal custo;
    private BigDecimal lucro;

    public LucroProduto(){}

    public LucroProduto(Long idProduto, String nome, BigDecimal receita, BigDecimal custo, BigDecimal lucro) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.receita = receita;
        this.custo = custo;
        this.lucro = lucro;
    }
    
    public Long getIdProduto() {
        return idProduto;
    }
    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public BigDecimal getReceita() {
        return receita;
    }
    public void setReceita(BigDecimal receita) {
        this.receita = receita;
    }
    public BigDecimal getCusto() {
        return custo;
    }
    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }
    public BigDecimal getLucro() {
        return lucro;
    }
    public void setLucro(BigDecimal lucro) {
        this.lucro = lucro;
    }

    
}
