package com.example.espacomix.controller;

import com.example.espacomix.model.Estoque;
import com.example.espacomix.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
@CrossOrigin(origins = "http://localhost:5173")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;



    @GetMapping
    public ResponseEntity<List<Estoque>> listarTudo() {
        return ResponseEntity.ok(estoqueService.listarTudo());
    }



    @GetMapping("/produto/{idProduto}")
    public ResponseEntity<Estoque> buscaPorProdutoId(@PathVariable Long idProduto) {
        return ResponseEntity.ok(estoqueService.buscaPorProdutoId(idProduto));
    }



    @PutMapping("/produto/{idProduto}/adicionar")
    public ResponseEntity<Estoque> adicionarQuantidade(
            @PathVariable Long idProduto,
            @RequestParam Integer quantidade) {
        Estoque estoqueAtualizado = estoqueService.adicionarQuantidade(idProduto, quantidade);
        return ResponseEntity.ok(estoqueAtualizado);
    }



    @PutMapping("/produto/{idProduto}/baixar")
    public ResponseEntity<Estoque> darBaixaQuantidade(
            @PathVariable Long idProduto,
            @RequestParam Integer quantidade) {
        Estoque estoqueAtualizado = estoqueService.darBaixaQuantidade(idProduto, quantidade);
        return ResponseEntity.ok(estoqueAtualizado);
    }
}

