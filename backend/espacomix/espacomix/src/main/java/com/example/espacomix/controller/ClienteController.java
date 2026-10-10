package com.example.espacomix.controller;

import com.example.espacomix.model.Cliente;
import com.example.espacomix.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;



    @PostMapping
    public ResponseEntity<Cliente> salvar(@RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.salvar(cliente);
        return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
    }



    @GetMapping
    public ResponseEntity<List<Cliente>> listarTudo() {
        return ResponseEntity.ok(clienteService.listarTudo());
    }



    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscaId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscaId(id));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

