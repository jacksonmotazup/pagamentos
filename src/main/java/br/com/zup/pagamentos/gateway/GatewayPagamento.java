package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.transacao.Transacao;

import java.math.BigDecimal;

/**
 * - Gateway Seya opera no brasil, aceita todas bandeiras, tem custo fixo de 6.00.
 * - Gateway Saori opera no brasil, só aceita visa e master, tem taxa percentual de 5% sobre a compra
 * - Gateway Tango opera na argentina, aceita todas as bandeiras e tem custo que varia entre fixo e variável.
 * Para compras de até 100 reais, o custo é fixo de 4.00. Para operações com valor maior que 100,
 * o custo vira de 6% do valor total.
 */
public interface GatewayPagamento {

    BigDecimal calculaTaxa(BigDecimal valor);

    RespostaTransacaoGateway processaPagamento(Transacao transacao);
}
