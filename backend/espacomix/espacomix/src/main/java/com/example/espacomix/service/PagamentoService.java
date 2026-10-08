package com.example.espacomix.service;

import com.example.espacomix.model.Pagamento;
import com.example.espacomix.model.Pedido;
import com.example.espacomix.repository.PagamentoRepository;
import com.example.espacomix.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public Pagamento salvar(Pagamento pagamento) {
        if (pagamento.getPedido() == null || pagamento.getPedido().getId() == null) {
            throw new RuntimeException("O PEDIDO E OBRIGATORIO PARA PROCESSAR O PAGAMENTO");
        }

        Long idPedido = pagamento.getPedido().getId();
        Pedido pedidoExistente = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("PEDIDO COM O ID " + idPedido + " NAO FOI ENCONTRADO"));

        if (pagamento.getValorPago() == null || pagamento.getValorPago().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O VALOR DO PAGAMENTO DEVE SER MAIOR QUE ZERO");
        }

        if (pagamento.getMetodoPagamento() == null || pagamento.getMetodoPagamento().trim().isEmpty()) {
            throw new RuntimeException("O METODO DE PAGAMENTO E OBRIGATORIO");
        }

        if (pagamento.getStatusPagamento() == null || pagamento.getStatusPagamento().trim().isEmpty()) {
            throw new RuntimeException("O STATUS DO PAGAMENTO E OBRIGATORIO");
        }

        if (pagamento.getDataPagamento() == null) {
            pagamento.setDataPagamento(LocalDateTime.now());
        }
        pagamento.setPedido(pedidoExistente);

        return pagamentoRepository.save(pagamento);
    }



    public List<Pagamento> listarTudo() {
        return pagamentoRepository.findAll();
    }



    public Pagamento buscaId(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PAGAMENTO COM O ID " + id + " NAO FOI ENCONTRADO"));
    }



    public Pagamento buscaPorPedidoId(Long idPedido) {
        if (!pedidoRepository.existsById(idPedido)) {
            throw new RuntimeException("PEDIDO COM O ID " + idPedido + " NAO FOI ENCONTRADO");
        }
        return pagamentoRepository.findByPedidoId(idPedido)
                .orElseThrow(() -> new RuntimeException("PAGAMENTO NAO ENCONTRADO PARA O PEDIDO COM O ID " + idPedido));
    }



    public void deletar(Long id) {
        Pagamento pagamento = buscaId(id);
        pagamentoRepository.delete(pagamento);
    }
}