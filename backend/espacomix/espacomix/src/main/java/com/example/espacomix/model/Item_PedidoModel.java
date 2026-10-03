package com.example.espacomix.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido")
public class Item_PedidoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "preco_unitario", precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "quantidade_de_itens_pedidos")
    private Integer quantidadeDeItensPedidos;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoModel pedido;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private ProdutoModel produto;

    public Item_PedidoModel() {
    }

    public Item_PedidoModel(Long id, BigDecimal precoUnitario, Integer quantidadeDeItensPedidos, PedidoModel pedido, ProdutoModel produto) {
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

    public PedidoModel getPedido() {
        return pedido;
    }

    public void setPedido(PedidoModel pedido) {
        this.pedido = pedido;
    }

    public ProdutoModel getProduto() {
        return produto;
    }

    public void setProduto(ProdutoModel produto) {
        this.produto = produto;
    }
}