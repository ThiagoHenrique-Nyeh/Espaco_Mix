
package com.example.espacomix.service;

import com.example.espacomix.model.Cliente;
import com.example.espacomix.model.Item_Pedido;
import com.example.espacomix.model.Pedido;
import com.example.espacomix.model.Produto;
import com.example.espacomix.repository.ClienteRepository;
import com.example.espacomix.repository.Item_PedidoRepository;
import com.example.espacomix.repository.PedidoRepository;
import com.example.espacomix.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    private ClienteRepository clienteRepository;

    private Cliente buscarClienteAutenticado() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getName() == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "USUARIO NAO AUTENTICADO");
        }

        return clienteRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "CLIENTE NAO ENCONTRADO"));
    }

    private Pedido buscarPedidoDoCliente(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "PEDIDO NAO ENCONTRADO"));

        Cliente cliente = buscarClienteAutenticado();

        if (!pedido.getCliente().getId().equals(cliente.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "VOCE NAO TEM PERMISSAO PARA ALTERAR ESSE PEDIDO");
        }

        if (!"PENDENTE".equalsIgnoreCase(pedido.getStatusPedido())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "NAO E POSSIVEL ALTERAR UM PEDIDO QUE NAO ESTA PENDENTE");
        }

        return pedido;
    }

    @Transactional
    public Item_Pedido salvar(Item_Pedido itemPedido) {

        if (itemPedido.getPedido() == null ||
                itemPedido.getPedido().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O PEDIDO E OBRIGATORIO");
        }

        Long idPedido = itemPedido.getPedido().getId();

        Pedido pedido = buscarPedidoDoCliente(idPedido);

        if (itemPedido.getProduto() == null ||
                itemPedido.getProduto().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O PRODUTO E OBRIGATORIO");
        }

        Long idProduto = itemPedido.getProduto().getId();

        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "PRODUTO NAO ENCONTRADO"));

        Integer quantidade = itemPedido.getQuantidadeDeItensPedidos();

        if (quantidade == null || quantidade <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A QUANTIDADE DEVE SER MAIOR QUE ZERO");
        }

        if (produto.getPreco() == null ||
                produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O PRECO DO PRODUTO E INVALIDO");
        }

        Item_Pedido itemExistente = item_PedidoRepository
                .findByPedidoIdAndProdutoId(idPedido, idProduto)
                .orElse(null);

        BigDecimal valorAdicionado;

        if (itemExistente != null) {

            itemExistente.setQuantidadeDeItensPedidos(
                    itemExistente.getQuantidadeDeItensPedidos() + quantidade);

            valorAdicionado = itemExistente.getPrecoUnitario()
                    .multiply(BigDecimal.valueOf(quantidade));

            item_PedidoRepository.save(itemExistente);

            pedido.setValorTotal(
                    pedido.getValorTotal().add(valorAdicionado));

            pedidoRepository.save(pedido);

            return itemExistente;
        }

        Item_Pedido novoItem = new Item_Pedido();

        novoItem.setPedido(pedido);
        novoItem.setProduto(produto);
        novoItem.setPrecoUnitario(produto.getPreco());
        novoItem.setQuantidadeDeItensPedidos(quantidade);

        valorAdicionado = produto.getPreco()
                .multiply(BigDecimal.valueOf(quantidade));

        Item_Pedido itemSalvo = item_PedidoRepository.save(novoItem);

        pedido.setValorTotal(
                pedido.getValorTotal().add(valorAdicionado));

        pedidoRepository.save(pedido);

        return itemSalvo;
    }

    public List<Item_Pedido> listarTudo() {
        return item_PedidoRepository.findAll();
    }

    public List<Item_Pedido> listarPorPedidoId(Long idPedido) {

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "PEDIDO NAO ENCONTRADO"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean administrador = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!administrador) {
            Cliente cliente = buscarClienteAutenticado();

            if (!pedido.getCliente().getId().equals(cliente.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "VOCE NAO TEM PERMISSAO PARA CONSULTAR ESSE CARRINHO");
            }
        }

        return item_PedidoRepository.findByPedidoId(idPedido);
    }

    public Item_Pedido buscaId(Long id) {

        Item_Pedido item = item_PedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "ITEM NAO ENCONTRADO"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean administrador = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (administrador) {
            return item;
        }

        Cliente cliente = buscarClienteAutenticado();

        if (!item.getPedido().getCliente().getId().equals(cliente.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "VOCE NAO TEM PERMISSAO PARA CONSULTAR ESSE ITEM");
        }

        return item;
    }

    @Transactional
    public void deletar(Long id) {

        Item_Pedido item = item_PedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "ITEM NAO ENCONTRADO"));

        Pedido pedido = buscarPedidoDoCliente(item.getPedido().getId());

        BigDecimal subtotalItem = item.getPrecoUnitario()
                .multiply(BigDecimal.valueOf(
                        item.getQuantidadeDeItensPedidos()));

        BigDecimal novoTotal = pedido.getValorTotal()
                .subtract(subtotalItem);

        if (novoTotal.compareTo(BigDecimal.ZERO) < 0) {
            novoTotal = BigDecimal.ZERO;
        }

        item_PedidoRepository.delete(item);

        pedido.setValorTotal(novoTotal);

        pedidoRepository.save(pedido);
    }
}
