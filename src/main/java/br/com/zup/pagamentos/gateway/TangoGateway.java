package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.transacao.Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
    public RespostaTransacaoGateway processaPagamento(Transacao transacao) throws InterruptedException {
        var gatewayUtils = new GatewayUtils();
        gatewayUtils.congelaThread(50, 100);

        gatewayUtils.simulaException(this.getClass().getSimpleName());

        return gatewayUtils.criaRespostaTransacao(transacao, this);
    }


}
