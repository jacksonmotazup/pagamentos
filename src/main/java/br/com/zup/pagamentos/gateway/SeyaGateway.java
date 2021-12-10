package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.transacao.Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SeyaGateway implements GatewayPagamento {

    @Override
    public BigDecimal calculaTaxa(BigDecimal valor) {
        return BigDecimal.valueOf(6).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public RespostaTransacaoGateway processaPagamento(Transacao transacao) {
        var gatewayUtils = new GatewayUtils();
        gatewayUtils.congelaThread(10, 50);

        gatewayUtils.simulaException(this.getClass().getSimpleName());

        var taxa = this.calculaTaxa(transacao.getValor());

        return new RespostaTransacaoGateway(UUID.randomUUID(),
                this,
                taxa.setScale(2, RoundingMode.HALF_UP),
                LocalDateTime.now(),
                transacao.getPedidoId());
    }


}
