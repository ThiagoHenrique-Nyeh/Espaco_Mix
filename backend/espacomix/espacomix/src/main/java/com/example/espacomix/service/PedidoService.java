
package com.example.espacomix.service;

import com.example.espacomix.model.Cliente;
import com.example.espacomix.model.Pedido;
import com.example.espacomix.repository.ClienteRepository;
import com.example.espacomix.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;



    private Cliente buscarClienteAutenticado() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getName() == null) {

            throw new RuntimeException("USUARIO NAO AUTENTICADO");
        }

        String email = authentication.getName();

        return clienteRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("CLIENTE NAO ENCONTRADO"));
    }



    public Pedido salvar(Pedido pedido) {

        Cliente clienteAutenticado = buscarClienteAutenticado();

        pedido.setCliente(clienteAutenticado);

        if (pedido.getValorTotal() == null) {
            pedido.setValorTotal(BigDecimal.ZERO);

        } else if (pedido.getValorTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException(
                    "O VALOR TOTAL DO PEDIDO NAO PODE SER NEGATIVO");
        }

        if (pedido.getDataEHoraPedido() == null) {
            pedido.setDataEHoraPedido(OffsetDateTime.now());
        }

        if (pedido.getStatusPedido() == null ||
                pedido.getStatusPedido().trim().isEmpty()) {

            pedido.setStatusPedido("PENDENTE");
        }

        return pedidoRepository.save(pedido);
    }



    public List<Pedido> listarTudo() {
        return pedidoRepository.findAll();
    }



    public List<Pedido> listarPorClienteId(Long idCliente) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            return pedidoRepository.findByClienteId(idCliente);
        }

        Cliente clienteAutenticado = buscarClienteAutenticado();

        if (!clienteAutenticado.getId().equals(idCliente)) {
            throw new RuntimeException(
                    "VOCE NAO TEM PERMISSAO PARA CONSULTAR ESSES PEDIDOS");
        }
        return pedidoRepository.findByClienteId(idCliente);
    }



    public Pedido buscaId(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "PEDIDO COM O ID " + id + " NAO FOI ENCONTRADO"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return pedido;
        }



        Cliente clienteAutenticado = buscarClienteAutenticado();
        if (!pedido.getCliente().getId()
                .equals(clienteAutenticado.getId())) {
            throw new RuntimeException(
                    "VOCE NAO TEM PERMISSAO PARA CONSULTAR ESSE PEDIDO");
        }
        return pedido;
    }



    public Pedido atualizarStatus(Long id, String novoStatus) {

        if (novoStatus == null || novoStatus.trim().isEmpty()) {
            throw new RuntimeException("O NOVO STATUS NAO PODE SER VAZIO");
        }
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "PEDIDO COM O ID " + id + " NAO FOI ENCONTRADO"));
        pedido.setStatusPedido(novoStatus.toUpperCase());

        return pedidoRepository.save(pedido);
    }



    public void deletar(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "PEDIDO COM O ID " + id + " NAO FOI ENCONTRADO"));

        pedidoRepository.delete(pedido);
    }
}
