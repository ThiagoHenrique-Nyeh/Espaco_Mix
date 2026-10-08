package com.example.espacomix.repository;
import java.util.Optional;
import com.example.espacomix.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque,Long> {

    Optional<Estoque> findByProdutoId(Long produtoId);
}
