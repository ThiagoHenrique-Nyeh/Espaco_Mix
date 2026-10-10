package com.example.espacomix.controller;

import com.example.espacomix.model.Produto;
import com.example.espacomix.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
@CrossOrigin(origins = "http://localhost:5173")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;



    @PostMapping
    public ResponseEntity<Produto> salvar(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.salvar(produto);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }



    @GetMapping
    public ResponseEntity<List<Produto>> listarTudo() {
        return ResponseEntity.ok(produtoService.listarTudo());
    }



    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscaId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscaId(id));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

