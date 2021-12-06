package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.compartilhado.exceptions.GatewayOfflineException;
import br.com.zup.pagamentos.transacao.Transacao;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class GatewayUtils {

    private final Random random = new Random();

    public void simulaException(String classe) {
        var simulaPossivelException = this.random.nextInt(1, 10);
        if (simulaPossivelException == 1) {
            throw new GatewayOfflineException("Gateway " + classe + " está offline.");
        }
    }

    public void congelaThread(int de, int ate) throws InterruptedException {
        var tempo = this.random.nextInt(de, ate);
        Thread.sleep(tempo);
    }

    public RespostaTransacaoGateway criaRespostaTransacao(Transacao transacao, GatewayPagamento gateway) {
        var taxa = gateway.calculaTaxa(transacao.getValor());

        return new RespostaTransacaoGateway(UUID.randomUUID(),
                gateway,
                taxa.setScale(2, RoundingMode.HALF_UP),
                LocalDateTime.now(),
                transacao.getPedidoId());
    }
}
