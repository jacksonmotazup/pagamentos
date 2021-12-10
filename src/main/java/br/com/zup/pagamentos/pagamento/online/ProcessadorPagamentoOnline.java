package br.com.zup.pagamentos.pagamento.online;

import br.com.zup.pagamentos.gateway.RedirecionadorPagamentoOnline;
import br.com.zup.pagamentos.pagamento.PedidoMock;
import br.com.zup.pagamentos.transacao.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProcessadorPagamentoOnline {

    private final TransacaoRepository transacaoRepository;
    private final RedirecionadorPagamentoOnline gateway;

    @Autowired
    public ProcessadorPagamentoOnline(TransacaoRepository transacaoRepository,
                                      RedirecionadorPagamentoOnline gateway) {
        this.transacaoRepository = transacaoRepository;
        this.gateway = gateway;
    }

    @Async
    @Transactional
    public void processaPagamento(Long transacaoId, Long pedidoId) {
        var pedido = PedidoMock.paraPedido(pedidoId);

        var transacao = transacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new RuntimeException("por algum motivo a transação não foi encontrada"));

        try {
            transacao.adicionaValor(pedido.valor());
            var respostaTransacao = gateway.processaPagamento(transacao);
            transacao.concluiTransacaoOnline(respostaTransacao.taxa());
        } catch (Exception ex) {
            transacao.marcaComoErro();
            System.out.println(ex.getMessage());
        }

    }
}
