package com.example.espacomix.controller;

import com.example.espacomix.model.Item_Pedido;
import com.example.espacomix.service.Item_PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itempedido")
@CrossOrigin(origins = "http://localhost:5173")
public class Item_PedidoController {

    @Autowired
    private Item_PedidoService item_PedidoService;



    @PostMapping
    public ResponseEntity<Item_Pedido> salvar(@RequestBody Item_Pedido itemPedido) {
        Item_Pedido novoItem = item_PedidoService.salvar(itemPedido);
        return new ResponseEntity<>(novoItem, HttpStatus.CREATED);
    }



    @GetMapping
    public ResponseEntity<List<Item_Pedido>> listarTudo() {
        return ResponseEntity.ok(item_PedidoService.listarTudo());
    }



    @GetMapping("/{id}")
    public ResponseEntity<Item_Pedido> buscaId(@PathVariable Long id) {
        return ResponseEntity.ok(item_PedidoService.buscaId(id));
    }



    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<List<Item_Pedido>> listarPorPedidoId(@PathVariable Long idPedido) {
        return ResponseEntity.ok(item_PedidoService.listarPorPedidoId(idPedido));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        item_PedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

