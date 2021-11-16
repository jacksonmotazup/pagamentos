package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.transacao.Transacao;

import java.math.BigDecimal;

public interface GatewayPagamento {

    BigDecimal calculaTaxa(BigDecimal valor);

    RespostaTransacaoGateway processaPagamento(Transacao transacao);
}
