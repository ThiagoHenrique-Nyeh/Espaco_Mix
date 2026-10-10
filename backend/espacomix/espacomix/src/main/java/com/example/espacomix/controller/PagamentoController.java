package com.example.espacomix.controller;

import com.example.espacomix.model.Pagamento;
import com.example.espacomix.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamento")
@CrossOrigin(origins = "http://localhost:5173")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;



    @PostMapping
    public ResponseEntity<Pagamento> salvar(@RequestBody Pagamento pagamento) {
        Pagamento novoPagamento = pagamentoService.salvar(pagamento);
        return new ResponseEntity<>(novoPagamento, HttpStatus.CREATED);
    }



    @GetMapping
    public ResponseEntity<List<Pagamento>> listarTudo() {
        return ResponseEntity.ok(pagamentoService.listarTudo());
    }



    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscaId(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.buscaId(id));
    }



    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<Pagamento> buscaPorPedidoId(@PathVariable Long idPedido) {
        return ResponseEntity.ok(pagamentoService.buscaPorPedidoId(idPedido));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

