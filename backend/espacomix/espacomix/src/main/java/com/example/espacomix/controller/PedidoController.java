package com.example.espacomix.controller;

import com.example.espacomix.model.Pedido;
import com.example.espacomix.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
@CrossOrigin(origins = "http://localhost:5173")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;



    @PostMapping
    public ResponseEntity<Pedido> salvar(@RequestBody Pedido pedido) {
        Pedido novoPedido = pedidoService.salvar(pedido);
        return new ResponseEntity<>(novoPedido, HttpStatus.CREATED);
    }



    @GetMapping
    public ResponseEntity<List<Pedido>> listarTudo() {
        return ResponseEntity.ok(pedidoService.listarTudo());
    }



    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscaId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscaId(id));
    }



    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Pedido>> listarPorClienteId(@PathVariable Long idCliente) {
        return ResponseEntity.ok(pedidoService.listarPorClienteId(idCliente));
    }



    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatus(
            @PathVariable Long id,
            @RequestParam String novoStatus) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, novoStatus));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

