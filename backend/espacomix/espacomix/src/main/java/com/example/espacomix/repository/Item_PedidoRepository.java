package com.example.espacomix.repository;

import com.example.espacomix.model.ClienteModel;
import com.example.espacomix.model.Item_PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Item_PedidoRepository extends JpaRepository<Item_PedidoModel,Long> {
}
