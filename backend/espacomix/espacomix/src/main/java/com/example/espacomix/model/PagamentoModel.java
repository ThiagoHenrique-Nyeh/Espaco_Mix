package com.example.espacomix.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
public class PagamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoModel pedido;

    @Column(name = "metodo_pagamento", length = 50)
    private String metodoPagamento;

    @Column(name = "status_pagamento", length = 50)
    private String statusPagamento;

    @Column(name = "valor_pago", precision = 10, scale = 2)
    private BigDecimal valorPago;

    @Column(name = "numero_parcelas")
    private Integer numeroParcelas;

    @Column(name = "valor_parcelas", precision = 10, scale = 2)
    private BigDecimal valorParcelas;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    public PagamentoModel() {
    }

    public PagamentoModel(Long id, PedidoModel pedido, String metodoPagamento, String statusPagamento,
                          BigDecimal valorPago, Integer numeroParcelas, BigDecimal valorParcelas, LocalDateTime dataPagamento) {
        this.id = id;
        this.pedido = pedido;
        this.metodoPagamento = metodoPagamento;
        this.statusPagamento = statusPagamento;
        this.valorPago = valorPago;
        this.numeroParcelas = numeroParcelas;
        this.valorParcelas = valorParcelas;
        this.dataPagamento = dataPagamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PedidoModel getPedido() {
        return pedido;
    }

    public void setPedido(PedidoModel pedido) {
        this.pedido = pedido;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public Integer getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(Integer numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public BigDecimal getValorParcelas() {
        return valorParcelas;
    }

    public void setValorParcelas(BigDecimal valorParcelas) {
        this.valorParcelas = valorParcelas;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}