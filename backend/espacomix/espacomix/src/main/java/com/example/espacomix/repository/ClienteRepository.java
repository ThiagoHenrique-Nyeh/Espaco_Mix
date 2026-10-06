package com.example.espacomix.repository;

import com.example.espacomix.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteModel,Long> {
}
