package com.example.espacomix.controller;

import com.example.espacomix.model.Administrador;
import com.example.espacomix.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrador")
@CrossOrigin(origins = "http://localhost:5173")

public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;



    @PostMapping
    public ResponseEntity<Administrador> salvar(@RequestBody Administrador administrador){
        Administrador novoAdministrador = administradorService.salvar(administrador);
            return new ResponseEntity<>(novoAdministrador, HttpStatus.CREATED);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Administrador> buscaId(@PathVariable Long id) {
        return ResponseEntity.ok(administradorService.buscaId(id));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        administradorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}