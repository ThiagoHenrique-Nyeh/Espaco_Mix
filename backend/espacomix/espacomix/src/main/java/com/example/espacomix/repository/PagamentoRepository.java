package com.example.espacomix.repository;

import com.example.espacomix.model.ClienteModel;
import com.example.espacomix.model.PagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<PagamentoModel,Long> {
}
