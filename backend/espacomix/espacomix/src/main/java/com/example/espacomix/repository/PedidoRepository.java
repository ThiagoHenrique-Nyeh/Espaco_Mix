package com.example.espacomix.repository;

import com.example.espacomix.model.ClienteModel;
import com.example.espacomix.model.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<PedidoModel,Long> {
}
