package com.example.espacomix.repository;

import com.example.espacomix.model.EstoqueModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<EstoqueModel,Long> {
}
