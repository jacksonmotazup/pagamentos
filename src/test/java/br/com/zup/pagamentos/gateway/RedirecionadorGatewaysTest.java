package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.transacao.Transacao;
import br.com.zup.pagamentos.usuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;

import static br.com.zup.pagamentos.formapagamento.FormaPagamento.CARTAO_CREDITO;
import static br.com.zup.pagamentos.transacao.StatusTransacao.EM_PROCESSAMENTO;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * - Gateway Seya opera no brasil, aceita todas bandeiras, tem custo fixo de 6.00.
 * - Gateway Saori opera no brasil, só aceita visa e master, tem taxa percentual de 5% sobre a compra
 * - Gateway Tango opera na argentina, aceita todas as bandeiras e tem custo que varia entre fixo e variável.
 * Para compras de até 100 reais, o custo é fixo de 4.00. Para operações com valor maior que 100,
 * o custo vira de 6% do valor total.
 */
class RedirecionadorGatewaysTest {

    private final Collection<GatewayPagamento> gateways = List.of(new SaoriGateway(), new SeyaGateway(), new TangoGateway());
    private final RedirecionadorGateways redirecionador = new RedirecionadorGateways(gateways);

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 1.00")
    void teste1() {
        var transacao = criaTransacao(1.00);

        var transacaoProcessada = redirecionador.processaPagamento(transacao);

        assertEquals(valorBigDecimal(0.950), transacaoProcessada.getValor());
    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 80.00")
    void teste2() {
        var transacao = criaTransacao(80.00);

        var transacaoProcessada = redirecionador.processaPagamento(transacao);

        assertEquals(valorBigDecimal(76.00), transacaoProcessada.getValor());
    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 81.00")
    void teste3() {
        var transacao = criaTransacao(81.00);

        var transacaoProcessada = redirecionador.processaPagamento(transacao);

        assertEquals(valorBigDecimal(77.00), transacaoProcessada.getValor());
    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 100.00")
    void teste4() {
        var transacao = criaTransacao(100.00);

        var transacaoProcessada = redirecionador.processaPagamento(transacao);

        assertEquals(valorBigDecimal(96.00), transacaoProcessada.getValor());
    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 101.00")
    void teste5() {
        var transacao = criaTransacao(101.00);

        var transacaoProcessada = redirecionador.processaPagamento(transacao);

        assertEquals(valorBigDecimal(95.95), transacaoProcessada.getValor());
    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 120.00")
    void teste6() {
        var transacao = criaTransacao(120.00);

        var transacaoProcessada = redirecionador.processaPagamento(transacao);

        assertEquals(valorBigDecimal(114.00), transacaoProcessada.getValor());
    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 121.00")
    void teste7() {
        var transacao = criaTransacao(121.00);

        var transacaoProcessada = redirecionador.processaPagamento(transacao);

        assertEquals(valorBigDecimal(115.00), transacaoProcessada.getValor());
    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 200.00")
    void teste8() {
        var transacao = criaTransacao(200.00);

        var transacaoProcessada = redirecionador.processaPagamento(transacao);

        assertEquals(valorBigDecimal(194.00), transacaoProcessada.getValor());
    }



    private Transacao criaTransacao(double valor) {
        return new Transacao(123L,
                new Usuario("email@a.com", CARTAO_CREDITO),
                new Restaurante("Restaurante", CARTAO_CREDITO),
                BigDecimal.valueOf(valor),
                CARTAO_CREDITO,
                null,
                EM_PROCESSAMENTO);
    }

    private BigDecimal valorBigDecimal(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_EVEN);
    }
}