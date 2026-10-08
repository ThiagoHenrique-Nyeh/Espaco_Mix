package com.example.espacomix.service;

import com.example.espacomix.model.Pedido;
import com.example.espacomix.repository.ClienteRepository;
import com.example.espacomix.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Pedido salvar(Pedido pedido) {

        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new RuntimeException("O CLIENTE E OBRIGATORIO PARA CRIAR O PEDIDO");
        }

        Long idCliente = pedido.getCliente().getId();
        if (!clienteRepository.existsById(idCliente)) {
            throw new RuntimeException("CLIENTE COM O ID " + idCliente + " NAO FOI ENCONTRADO");
        }


        if (pedido.getValorTotal() == null) {
            pedido.setValorTotal(BigDecimal.ZERO);
        } else if (pedido.getValorTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("O VALOR TOTAL DO PEDIDO NAO PODE SER NEGATIVO");
        }

        if (pedido.getDataEHoraPedido() == null) {
            pedido.setDataEHoraPedido(OffsetDateTime.now());
        }

        if (pedido.getStatusPedido() == null || pedido.getStatusPedido().trim().isEmpty()) {
            pedido.setStatusPedido("PENDENTE");
        }

        return pedidoRepository.save(pedido);
    }



    public List<Pedido> listarTudo() {
        return pedidoRepository.findAll();
    }



    public List<Pedido> listarPorClienteId(Long idCliente) {
        if (!clienteRepository.existsById(idCliente)) {
            throw new RuntimeException("CLIENTE COM O ID " + idCliente + " NAO FOI ENCONTRADO");
        }
        return pedidoRepository.findByClienteId(idCliente);
    }



    public Pedido buscaId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PEDIDO COM O ID " + id + " NAO FOI ENCONTRADO"));
    }



    public Pedido atualizarStatus(Long id, String novoStatus) {
        if (novoStatus == null || novoStatus.trim().isEmpty()) {
            throw new RuntimeException("O NOVO STATUS NAO PODE SER VAZIO");
        }
        Pedido pedido = buscaId(id);
        pedido.setStatusPedido(novoStatus.toUpperCase());

        return pedidoRepository.save(pedido);
    }



    public void deletar(Long id) {
        Pedido pedido = buscaId(id);
        pedidoRepository.delete(pedido);
    }
}