package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.transacao.Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class SaoriGateway implements GatewayPagamento {
    @Override
    public BigDecimal calculaTaxa(BigDecimal valor) {
        return valor.multiply(BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    public RespostaTransacaoGateway processaPagamento(Transacao transacao) throws InterruptedException {
        var gatewayUtils = new GatewayUtils();

        gatewayUtils.congelaThread(70, 150);

        gatewayUtils.simulaException(this.getClass().getSimpleName());

        return gatewayUtils.criaRespostaTransacao(transacao, this);
    }


}
