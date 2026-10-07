package com.example.espacomix.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido")
public class Item_Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "preco_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precoUnitario;

    @Column(name = "quantidade_de_itens_pedidos", nullable = false)
    private Integer quantidadeDeItensPedidos;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    public Item_Pedido() {
    }

    public Item_Pedido(Long id, BigDecimal precoUnitario, Integer quantidadeDeItensPedidos, Pedido pedido, Produto produto) {
        this.id = id;
        this.precoUnitario = precoUnitario;
        this.quantidadeDeItensPedidos = quantidadeDeItensPedidos;
        this.pedido = pedido;
        this.produto = produto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Integer getQuantidadeDeItensPedidos() {
        return quantidadeDeItensPedidos;
    }

    public void setQuantidadeDeItensPedidos(Integer quantidadeDeItensPedidos) {
        this.quantidadeDeItensPedidos = quantidadeDeItensPedidos;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}