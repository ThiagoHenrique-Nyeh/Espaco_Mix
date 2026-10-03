package com.example.espacomix.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estoque")
public class EstoqueModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private ProdutoModel produto;

    private Integer quantidade;

    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;

    public EstoqueModel() {
    }

    public EstoqueModel(Long id, ProdutoModel produto, Integer quantidade, LocalDateTime ultimaAtualizacao) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProdutoModel getProduto() {
        return produto;
    }

    public void setProduto(ProdutoModel produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
}