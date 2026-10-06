package com.example.espacomix.repository;

import com.example.espacomix.model.ClienteModel;
import com.example.espacomix.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoModel,Long> {
}
