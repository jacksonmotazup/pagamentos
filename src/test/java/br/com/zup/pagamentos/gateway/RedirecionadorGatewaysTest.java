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
import static org.junit.jupiter.api.Assertions.assertAll;
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
    @DisplayName("Deve redirecionar pro gateway com menor taxa com valor de pagamento 1.00 mantendo arredondamento")
    void teste1() throws InterruptedException {
        var transacao = criaTransacao(1.00);

        var respostaTransacao = redirecionador.processaPagamento(transacao);

        assertAll(
                () -> assertEquals(valorBigDecimal(0.05), respostaTransacao.taxa()),
                () -> assertEquals(SaoriGateway.class.getSimpleName(), respostaTransacao.gateway().getClass().getSimpleName())
        );
    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa com valor de pagamento 1.35 mantendo arredondamento")
    void teste2() throws InterruptedException {
        var transacao = criaTransacao(1.35);

        var respostaTransacao = redirecionador.processaPagamento(transacao);

        assertAll(
                () -> assertEquals(valorBigDecimal(0.07), respostaTransacao.taxa()),
                () -> assertEquals(SaoriGateway.class.getSimpleName(), respostaTransacao.gateway().getClass().getSimpleName())
        );
    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 80.00")
    void teste3() throws InterruptedException {
        var transacao = criaTransacao(80.00);

        var respostaTransacao = redirecionador.processaPagamento(transacao);

        assertAll(
                () -> assertEquals(valorBigDecimal(4.00), respostaTransacao.taxa()),
                () -> assertEquals(SaoriGateway.class.getSimpleName(), respostaTransacao.gateway().getClass().getSimpleName())
        );

    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 81.00")
    void teste4() throws InterruptedException {
        var transacao = criaTransacao(81.00);

        var respostaTransacao = redirecionador.processaPagamento(transacao);

        assertAll(
                () -> assertEquals(valorBigDecimal(4.00), respostaTransacao.taxa()),
                () -> assertEquals(TangoGateway.class.getSimpleName(), respostaTransacao.gateway().getClass().getSimpleName())
        );

    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 100.00")
    void teste5() throws InterruptedException {
        var transacao = criaTransacao(100.00);

        var respostaTransacao = redirecionador.processaPagamento(transacao);

        assertAll(
                () -> assertEquals(valorBigDecimal(4.00), respostaTransacao.taxa()),
                () -> assertEquals(TangoGateway.class.getSimpleName(), respostaTransacao.gateway().getClass().getSimpleName())
        );
    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 101.00")
    void teste6() throws InterruptedException {
        var transacao = criaTransacao(101.00);

        var respostaTransacao = redirecionador.processaPagamento(transacao);

        assertAll(
                () -> assertEquals(valorBigDecimal(5.05), respostaTransacao.taxa()),
                () -> assertEquals(SaoriGateway.class.getSimpleName(), respostaTransacao.gateway().getClass().getSimpleName())
        );

    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 120.00")
    void teste7() throws InterruptedException {
        var transacao = criaTransacao(120.00);

        var respostaTransacao = redirecionador.processaPagamento(transacao);

        assertAll(
                () -> assertEquals(valorBigDecimal(6.00), respostaTransacao.taxa()),
                () -> assertEquals(SaoriGateway.class.getSimpleName(), respostaTransacao.gateway().getClass().getSimpleName())
        );
    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 121.00")
    void teste8() throws InterruptedException {
        var transacao = criaTransacao(121.00);

        var respostaTransacao = redirecionador.processaPagamento(transacao);
        assertAll(
                () -> assertEquals(valorBigDecimal(6.00), respostaTransacao.taxa()),
                () -> assertEquals(SeyaGateway.class.getSimpleName(), respostaTransacao.gateway().getClass().getSimpleName())
        );

    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 200.00")
    void teste9() throws InterruptedException {
        var transacao = criaTransacao(200.00);

        var respostaTransacao = redirecionador.processaPagamento(transacao);
        assertAll(
                () -> assertEquals(valorBigDecimal(6.00), respostaTransacao.taxa()),
                () -> assertEquals(SeyaGateway.class.getSimpleName(), respostaTransacao.gateway().getClass().getSimpleName())
        );

    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa  com valor de pagamento 212.23")
    void teste10() throws InterruptedException {
        var transacao = criaTransacao(212.23);

        var respostaTransacao = redirecionador.processaPagamento(transacao);
        assertAll(
                () -> assertEquals(valorBigDecimal(6.00), respostaTransacao.taxa()),
                () -> assertEquals(SeyaGateway.class.getSimpleName(), respostaTransacao.gateway().getClass().getSimpleName())
        );

    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa com valor de pagamento 0.11 mantendo arredondamento")
    void teste11() throws InterruptedException {
        var transacao = criaTransacao(0.11);

        var respostaTransacao = redirecionador.processaPagamento(transacao);

        assertAll(
                () -> assertEquals(valorBigDecimal(0.01), respostaTransacao.taxa()),
                () -> assertEquals(SaoriGateway.class.getSimpleName(), respostaTransacao.gateway().getClass().getSimpleName())
        );
    }

    @Test
    @DisplayName("Deve redirecionar pro gateway com menor taxa com valor de pagamento 14.29 mantendo arredondamento")
    void teste12() throws InterruptedException {
        var transacao = criaTransacao(14.29);

        var respostaTransacao = redirecionador.processaPagamento(transacao);

        assertAll(
                () -> assertEquals(valorBigDecimal(0.71), respostaTransacao.taxa()),
                () -> assertEquals(SaoriGateway.class.getSimpleName(), respostaTransacao.gateway().getClass().getSimpleName())
        );
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

    private BigDecimal valorBigDecimal(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_EVEN);
    }
}