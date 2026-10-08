package com.example.espacomix.repository;

import com.example.espacomix.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido,Long> {
    List<Pedido> findByClienteId(Long idCliente);
}
