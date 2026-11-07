
package com.example.espacomix.service;

import com.example.espacomix.model.Pagamento;
import com.example.espacomix.model.Pedido;
import com.example.espacomix.model.Item_Pedido;
import com.example.espacomix.model.Cliente;
import com.example.espacomix.repository.PagamentoRepository;
import com.example.espacomix.repository.PedidoRepository;
import com.example.espacomix.repository.Item_PedidoRepository;
import com.example.espacomix.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private Item_PedidoRepository itemPedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EstoqueService estoqueService;

    @Transactional
    public Pagamento salvar(Pagamento pagamento) {

        if (pagamento.getPedido() == null || pagamento.getPedido().getId() == null) {
            throw new RuntimeException("O PEDIDO E OBRIGATORIO PARA PROCESSAR O PAGAMENTO");
        }

        Long idPedido = pagamento.getPedido().getId();

        Pedido pedidoExistente = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("PEDIDO COM O ID " + idPedido + " NAO FOI ENCONTRADO"));

        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();

        if (autenticacao == null || !autenticacao.isAuthenticated()) {
            throw new AccessDeniedException("USUARIO NAO AUTENTICADO");
        }

        String email = autenticacao.getName();

        Cliente clienteAutenticado = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("CLIENTE NAO ENCONTRADO"));

        if (pedidoExistente.getCliente() == null ||
                !pedidoExistente.getCliente().getId().equals(clienteAutenticado.getId())) {
            throw new AccessDeniedException("VOCE NAO TEM PERMISSAO PARA PAGAR ESTE PEDIDO");
        }

        if (pagamentoRepository.findByPedidoId(idPedido).isPresent()) {
            throw new RuntimeException("ESTE PEDIDO JA POSSUI UM PAGAMENTO REGISTRADO");
        }

        if (!"PENDENTE".equalsIgnoreCase(pedidoExistente.getStatusPedido())) {
            throw new RuntimeException("SOMENTE PEDIDOS PENDENTES PODEM SER PAGOS");
        }

        List<Item_Pedido> itensPedido = itemPedidoRepository.findByPedidoId(idPedido);

        if (itensPedido.isEmpty()) {
            throw new RuntimeException("NAO E POSSIVEL PAGAR UM PEDIDO SEM ITENS");
        }

        if (pedidoExistente.getValorTotal() == null ||
                pedidoExistente.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O VALOR TOTAL DO PEDIDO DEVE SER MAIOR QUE ZERO");
        }

        if (pagamento.getValorPago() == null ||
                pagamento.getValorPago().compareTo(pedidoExistente.getValorTotal()) != 0) {
            throw new RuntimeException("O VALOR PAGO DEVE SER IGUAL AO VALOR TOTAL DO PEDIDO");
        }

        if (pagamento.getMetodoPagamento() == null ||
                pagamento.getMetodoPagamento().trim().isEmpty()) {
            throw new RuntimeException("O METODO DE PAGAMENTO E OBRIGATORIO");
        }

        String metodo = pagamento.getMetodoPagamento().trim().toUpperCase(Locale.ROOT);

        if (metodo.equals("PIX") || metodo.equals("DEBITO")) {

            pagamento.setNumeroParcelas(1);
            pagamento.setValorParcelas(pedidoExistente.getValorTotal());

        } else if (metodo.equals("CREDITO")) {

            if (pagamento.getNumeroParcelas() == null ||
                    pagamento.getNumeroParcelas() <= 0) {
                pagamento.setNumeroParcelas(1);
            }

            BigDecimal numParcelas = BigDecimal.valueOf(pagamento.getNumeroParcelas());

            BigDecimal valorParcelaCalculado = pedidoExistente.getValorTotal()
                    .divide(numParcelas, 2, RoundingMode.HALF_UP);

            pagamento.setValorParcelas(valorParcelaCalculado);

        } else {
            throw new RuntimeException("METODO DE PAGAMENTO INVALIDO. USE PIX, DEBITO OU CREDITO");
        }

        for (Item_Pedido item : itensPedido) {

            Long idProduto = item.getProduto().getId();
            Integer quantidade = item.getQuantidadeDeItensPedidos();

            if (quantidade == null || quantidade <= 0) {
                throw new RuntimeException("QUANTIDADE INVALIDA NO ITEM DO PEDIDO");
            }

            if (!estoqueService.validarDisponibilidade(idProduto, quantidade)) {
                throw new RuntimeException("ESTOQUE INSUFICIENTE PARA O PRODUTO COM ID " + idProduto);
            }
        }

        for (Item_Pedido item : itensPedido) {

            Long idProduto = item.getProduto().getId();
            Integer quantidade = item.getQuantidadeDeItensPedidos();

            estoqueService.darBaixaQuantidade(idProduto, quantidade);
        }

        // [IMPORTANTE] FAZ O MEU BACKEND DEFINIR AUTOMATICAMENTE A APROVACAO
        pagamento.setStatusPagamento("APROVADO");
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setPedido(pedidoExistente);

        pedidoExistente.setStatusPedido("PAGO");
        pedidoRepository.save(pedidoExistente);

        return pagamentoRepository.save(pagamento);
    }

    public List<Pagamento> listarTudo() {
        return pagamentoRepository.findAll();
    }

    public Pagamento buscaId(Long id) {

        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PAGAMENTO COM O ID " + id + " NAO FOI ENCONTRADO"));

        validarAcessoAoPedido(pagamento.getPedido());

        return pagamento;
    }

    public Pagamento buscaPorPedidoId(Long idPedido) {

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("PEDIDO COM O ID " + idPedido + " NAO FOI ENCONTRADO"));

        validarAcessoAoPedido(pedido);

        return pagamentoRepository.findByPedidoId(idPedido)
                .orElseThrow(() -> new RuntimeException("PAGAMENTO NAO ENCONTRADO PARA O PEDIDO COM O ID " + idPedido));
    }

    private void validarAcessoAoPedido(Pedido pedido) {

        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();

        if (autenticacao == null || !autenticacao.isAuthenticated()) {
            throw new AccessDeniedException("USUARIO NAO AUTENTICADO");
        }

        boolean administrador = autenticacao.getAuthorities().stream()
                .anyMatch(autoridade -> autoridade.getAuthority().equals("ROLE_ADMIN"));

        if (administrador) {
            return;
        }

        Cliente clienteAutenticado = clienteRepository.findByEmail(autenticacao.getName())
                .orElseThrow(() -> new AccessDeniedException("CLIENTE NAO ENCONTRADO"));

        if (pedido.getCliente() == null ||
                !pedido.getCliente().getId().equals(clienteAutenticado.getId())) {
            throw new AccessDeniedException("VOCE NAO TEM PERMISSAO PARA CONSULTAR ESTE PAGAMENTO");
        }
    }

    public void deletar(Long id) {

        Pagamento pagamento = buscaId(id);
        pagamentoRepository.delete(pagamento);
    }
}
