package com.example.espacomix.service;

import com.example.espacomix.model.Pagamento;
import com.example.espacomix.model.Pedido;
import com.example.espacomix.repository.PagamentoRepository;
import com.example.espacomix.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
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
            pagamento.setStatusPagamento("PENDENTE");
        }

        if (pagamento.getValorPago().compareTo(pedidoExistente.getValorTotal()) != 0) {
            throw new RuntimeException("O VALOR PAGO DEVE SER IGUAL AO VALOR TOTAL DO PEDIDO");
        }

        if (pagamento.getMetodoPagamento()== null || pagamento.getMetodoPagamento().trim().isEmpty()){
            throw new RuntimeException("O MÉTODO DE PAGAMENTO É OBRIGATÓRIO");
        }

        String metodo = pagamento.getMetodoPagamento().toUpperCase();

        if (metodo.equals("PIX") || metodo.equals("DEBITO")) {
            pagamento.setNumeroParcelas(1);
            pagamento.setValorParcelas(pedidoExistente.getValorTotal());
        } else if (metodo.equals("CREDITO")) {
            if (pagamento.getNumeroParcelas() == null || pagamento.getNumeroParcelas() <= 0) {
                pagamento.setNumeroParcelas(1);
            }

            BigDecimal numParcelas = BigDecimal.valueOf(pagamento.getNumeroParcelas());
            BigDecimal valorParcelaCalculado = pedidoExistente.getValorTotal()
                    .divide(numParcelas, 2, RoundingMode.HALF_UP);
            pagamento.setValorParcelas(valorParcelaCalculado);
        } else {
            throw new RuntimeException("METODO DE PAGAMENTO INVALIDO. USE PIX, DEBITO OU CREDITO");
        }

        if (pagamento.getStatusPagamento() == null || pagamento.getStatusPagamento().trim().isEmpty()) {
            pagamento.setStatusPagamento("PENDENTE");
        }

        if (pagamento.getDataPagamento() == null) {
            pagamento.setDataPagamento(LocalDateTime.now());
        }

        pagamento.setPedido(pedidoExistente);

        pedidoExistente.setStatusPedido("PAGO");
        pedidoRepository.save(pedidoExistente);

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