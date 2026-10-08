package com.example.espacomix.service;

import com.example.espacomix.model.Estoque;
import com.example.espacomix.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    // --> [BUSCAR ESTOQUE PELO ID DO PRODUTO]
    public Estoque buscaPorProdutoId(Long idProduto) {
        return estoqueRepository.findByProdutoId(idProduto)
                .orElseThrow(() -> new RuntimeException("ESTOQUE NAO ENCONTRADO PARA O PRODUTO COM O ID " + idProduto));
    }



    // --> [ADICIONAR QUANTIDADE AO ESTOQUE]
    public Estoque adicionarQuantidade(Long idProduto, Integer quantidadeParaAdicionar) {
        if (quantidadeParaAdicionar == null || quantidadeParaAdicionar <= 0) {
            throw new RuntimeException("A QUANTIDADE A ADICIONAR DEVE SER MAIOR QUE ZERO");
        }
        Estoque estoque = buscaPorProdutoId(idProduto);
        estoque.setQuantidade(estoque.getQuantidade() + quantidadeParaAdicionar);
        estoque.setUltimaAtualizacao(LocalDateTime.now());

        return estoqueRepository.save(estoque);
    }



    // --> [REMOVER QUANTIDADE DO ESTOQUE / DAR BAIXA]
    public Estoque darBaixaQuantidade(Long idProduto, Integer quantidadeParaBaixar) {
        if (quantidadeParaBaixar == null || quantidadeParaBaixar <= 0) {
            throw new RuntimeException("A QUANTIDADE A DAR BAIXA DEVE SER MAIOR QUE ZERO");
        }
        Estoque estoque = buscaPorProdutoId(idProduto);

        if (estoque.getQuantidade() < quantidadeParaBaixar) {
            throw new RuntimeException("ESTOQUE INSUFICIENTE. DISPONIVEL: " + estoque.getQuantidade());
        }

        estoque.setQuantidade(estoque.getQuantidade() - quantidadeParaBaixar);
        estoque.setUltimaAtualizacao(LocalDateTime.now());

        return estoqueRepository.save(estoque);
    }



    // --> [ZERAR O ESTOQUE]
    public Estoque zerarEstoque(Long idProduto) {
        Estoque estoque = buscaPorProdutoId(idProduto);
        estoque.setQuantidade(0);
        estoque.setUltimaAtualizacao(LocalDateTime.now());

        return estoqueRepository.save(estoque);
    }



    // --> [VALIDAR DISPONIBILIDADE NO ESTOQUE]
    public boolean validarDisponibilidade(Long idProduto, Integer quantidadeDesejada) {
        Estoque estoque = buscaPorProdutoId(idProduto);
        return estoque.getQuantidade() >= quantidadeDesejada;
    }



    public List<Estoque> listarTudo() {return estoqueRepository.findAll();}
}