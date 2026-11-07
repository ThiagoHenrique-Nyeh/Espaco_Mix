
package com.example.espacomix.repository;

import com.example.espacomix.model.Item_Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Item_PedidoRepository extends JpaRepository<Item_Pedido, Long> {

    List<Item_Pedido> findByPedidoId(Long idPedido);

    Optional<Item_Pedido> findByPedidoIdAndProdutoId(Long idPedido, Long idProduto);
}
