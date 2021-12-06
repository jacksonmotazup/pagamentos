package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.compartilhado.exceptions.GatewayOfflineException;
import br.com.zup.pagamentos.transacao.Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class SaoriGateway implements GatewayPagamento {
    @Override
    public BigDecimal calculaTaxa(BigDecimal valor) {
        return valor.multiply(BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    public RespostaTransacaoGateway processaPagamento(Transacao transacao) throws InterruptedException {
        var random = new Random();

        var tempo = random.nextInt(70, 150);
        Thread.sleep(tempo);

        var simulaPossivelException = random.nextInt(1, 10);
        if (simulaPossivelException == 1){
            throw new GatewayOfflineException("Gateway "+ this.getClass().getSimpleName() + " está offline.");
        }

        var taxa = this.calculaTaxa(transacao.getValor());

        return new RespostaTransacaoGateway(UUID.randomUUID(),
                this,
                taxa.setScale(2, RoundingMode.HALF_UP),
                LocalDateTime.now(),
                transacao.getPedidoId());
    }

}
