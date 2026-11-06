
package com.example.espacomix.service;

import com.example.espacomix.model.Produto;
import com.example.espacomix.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;


    public Produto salvar(Produto produto) {

        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new RuntimeException("O NOME DO PRODUTO E OBRIGATORIO");
        }

        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O PRECO DO PRODUTO DEVE SER MAIOR QUE ZERO");
        }

        if (produto.getCategoria() == null || produto.getCategoria().trim().isEmpty()) {
            throw new RuntimeException("A CATEGORIA DO PRODUTO E OBRIGATORIA");
        }

        return produtoRepository.save(produto);
    }



    public List<Produto> listarTudo() {
        return produtoRepository.findAll();
    }



    public Produto buscaId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PRODUTO COM O ID " + id + " NAO FOI ENCONTRADO"));
    }



    public Produto atualizar(Long id, Produto novosDados) {

        Produto produtoExistente = buscaId(id);

        if (novosDados.getNome() == null || novosDados.getNome().trim().isEmpty()) {
            throw new RuntimeException("O NOME DO PRODUTO E OBRIGATORIO");
        }

        if (novosDados.getPreco() == null || novosDados.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O PRECO DO PRODUTO DEVE SER MAIOR QUE ZERO");
        }

        if (novosDados.getCategoria() == null || novosDados.getCategoria().trim().isEmpty()) {
            throw new RuntimeException("A CATEGORIA DO PRODUTO E OBRIGATORIA");
        }

        produtoExistente.setNome(novosDados.getNome());
        produtoExistente.setDescricao(novosDados.getDescricao());
        produtoExistente.setPreco(novosDados.getPreco());
        produtoExistente.setTamanho(novosDados.getTamanho());
        produtoExistente.setCor(novosDados.getCor());
        produtoExistente.setCategoria(novosDados.getCategoria());

        return produtoRepository.save(produtoExistente);
    }



    public void deletar(Long id) {
        Produto produto = buscaId(id);
        produtoRepository.delete(produto);
    }
}
