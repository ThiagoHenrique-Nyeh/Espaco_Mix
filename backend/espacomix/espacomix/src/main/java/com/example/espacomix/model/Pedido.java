package com.example.espacomix.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "data_e_hora_pedido", nullable = false)
    private OffsetDateTime dataEHoraPedido;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "status_pedido", length = 30, nullable = false)
    private String statusPedido;

    public Pedido() {
    }

    public Pedido(Long id, BigDecimal valorTotal, OffsetDateTime dataEHoraPedido, Cliente cliente, String statusPedido) {
        this.id = id;
        this.valorTotal = valorTotal;
        this.dataEHoraPedido = dataEHoraPedido;
        this.cliente = cliente;
        this.statusPedido = statusPedido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public OffsetDateTime getDataEHoraPedido() {
        return dataEHoraPedido;
    }

    public void setDataEHoraPedido(OffsetDateTime dataEHoraPedido) {
        this.dataEHoraPedido = dataEHoraPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(String statusPedido) {
        this.statusPedido = statusPedido;
    }
}