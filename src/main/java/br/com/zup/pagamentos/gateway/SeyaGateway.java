package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.transacao.Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class SeyaGateway implements GatewayPagamento {

    @Override
    public BigDecimal calculaTaxa(BigDecimal valor) {
        return BigDecimal.valueOf(6).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public RespostaTransacaoGateway processaPagamento(Transacao transacao) throws InterruptedException {
        var gatewayUtils = new GatewayUtils();
        gatewayUtils.congelaThread(10, 50);

        gatewayUtils.simulaException(this.getClass().getSimpleName());

        return gatewayUtils.criaRespostaTransacao(transacao, this);
    }


}
