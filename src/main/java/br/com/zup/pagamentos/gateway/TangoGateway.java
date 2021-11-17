package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.transacao.Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TangoGateway implements GatewayPagamento {
    @Override
    public BigDecimal calculaTaxa(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return BigDecimal.valueOf(4).setScale(2, RoundingMode.HALF_UP);
        }
        return valor.multiply(BigDecimal.valueOf(0.06).setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    public RespostaTransacaoGateway processaPagamento(Transacao transacao) {
        var taxa = this.calculaTaxa(transacao.getValor());

        return new RespostaTransacaoGateway(UUID.randomUUID(),
                this,
                taxa.setScale(2, RoundingMode.HALF_UP),
                LocalDateTime.now(),
                transacao.getPedidoId());
    }
}
