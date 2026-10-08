package com.example.espacomix.service;

import com.example.espacomix.model.Item_Pedido;
import com.example.espacomix.model.Pedido;
import com.example.espacomix.model.Produto;
import com.example.espacomix.repository.Item_PedidoRepository;
import com.example.espacomix.repository.PedidoRepository;
import com.example.espacomix.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class Item_PedidoService {

    @Autowired
    private Item_PedidoRepository item_PedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueService estoqueService;

    public Item_Pedido salvar(Item_Pedido itemPedido) {
        if (itemPedido.getPedido() == null || itemPedido.getPedido().getId() == null) {
            throw new RuntimeException("O PEDIDO É OBRIGATÓRIO PARA O ITEM_PEDIDO");
        }
        Long idPedido = itemPedido.getPedido().getId();
        Pedido pedidoExistente = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("PEDIDO COM O ID " + idPedido + " NAO FOI ENCONTRADO"));

        if (itemPedido.getProduto() == null || itemPedido.getProduto().getId() == null) {
            throw new RuntimeException("O PRODUTO É OBRIGATÓRIO PARA O ITEM_PEDIDO");
        }
        Long idProduto = itemPedido.getProduto().getId();
        Produto produtoExistente = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new RuntimeException("PRODUTO COM O ID " + idProduto + " NAO FOI ENCONTRADO"));

        if (itemPedido.getQuantidadeDeItensPedidos() == null || itemPedido.getQuantidadeDeItensPedidos() <= 0) {
            throw new RuntimeException("A QUANTIDADE DO ITEM_PEDIDO DEVE SER MAIOR QUE ZERO");
        }

        boolean temEstoque = estoqueService.validarDisponibilidade(idProduto, itemPedido.getQuantidadeDeItensPedidos());
        if (!temEstoque) {
            throw new RuntimeException("ESTOQUE INSUFICIENTE PARA O PRODUTO COM O ID " + idProduto);
        }

        if (itemPedido.getPrecoUnitario() == null) {
            itemPedido.setPrecoUnitario(produtoExistente.getPreco());
        } else if (itemPedido.getPrecoUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O PREÇO UNITÁRIO DEVE SER MAIOR QUE ZERO");
        }

        estoqueService.darBaixaQuantidade(idProduto, itemPedido.getQuantidadeDeItensPedidos());

        itemPedido.setPedido(pedidoExistente);
        itemPedido.setProduto(produtoExistente);

        return item_PedidoRepository.save(itemPedido);
    }



    public List<Item_Pedido> listarTudo() {
        return item_PedidoRepository.findAll();
    }



    public List<Item_Pedido> listarPorPedidoId(Long idPedido) {
        if (!pedidoRepository.existsById(idPedido)) {
            throw new RuntimeException("PEDIDO COM O ID " + idPedido + " NAO FOI ENCONTRADO");
        }
        return item_PedidoRepository.findByPedidoId(idPedido);
    }



    public Item_Pedido buscaId(Long id) {
        return item_PedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ITEM_PEDIDO COM O ID " + id + " NAO FOI ENCONTRADO"));
    }



    public void deletar(Long id) {
        Item_Pedido item = buscaId(id);
        estoqueService.adicionarQuantidade(item.getProduto().getId(), item.getQuantidadeDeItensPedidos());
        item_PedidoRepository.delete(item);
    }
}