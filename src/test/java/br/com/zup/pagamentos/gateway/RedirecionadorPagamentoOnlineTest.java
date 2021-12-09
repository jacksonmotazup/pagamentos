package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.compartilhado.exceptions.GatewayOfflineException;
import br.com.zup.pagamentos.compartilhado.exceptions.SemGatewayDisponivelException;
import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.transacao.Transacao;
import br.com.zup.pagamentos.usuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static br.com.zup.pagamentos.formapagamento.FormaPagamento.CARTAO_CREDITO;
import static br.com.zup.pagamentos.transacao.StatusTransacao.EM_PROCESSAMENTO;
import static java.math.RoundingMode.HALF_UP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RedirecionadorPagamentoOnlineTest {
    private final BigDecimal MENOR_VALOR = BigDecimal.valueOf(0.01).setScale(2, HALF_UP);
    private final BigDecimal VALOR_MEDIO = BigDecimal.valueOf(0.02).setScale(2, HALF_UP);
    private final BigDecimal MAIOR_VALOR = BigDecimal.valueOf(0.03).setScale(2, HALF_UP);

    private final GatewayPagamento gatewayMenorValor = mock(GatewayPagamento.class);
    private final GatewayPagamento gatewayMaiorValor = mock(GatewayPagamento.class);
    private final GatewayPagamento gatewayValorMedio = mock(GatewayPagamento.class);
    private final RedirecionadorPagamentoOnline redirecionador = new RedirecionadorPagamentoOnline(List.of(gatewayMenorValor,
            gatewayMaiorValor, gatewayValorMedio));

    @Nested
    class testesProcessaPagamento {

        @Test
        @DisplayName("Deve redirecionar pro gateway de menor taxa, quando todos estiverem disponiveis")
        void teste1() {
            var transacao = criaTransacao();
            var respostaGateway = criaRespostaGateway(gatewayMenorValor, MENOR_VALOR);

            mockGateways(transacao);
            when(gatewayMenorValor.processaPagamento(transacao)).thenReturn(respostaGateway);

            var respostaTransacaoGateway = redirecionador.processaPagamento(transacao);

            assertAll(
                    () -> assertEquals(respostaGateway.gateway().getClass(), respostaTransacaoGateway.gateway().getClass()),
                    () -> assertEquals(MENOR_VALOR, respostaTransacaoGateway.taxa())
            );
        }

        @Test
        @DisplayName("Deve redirecionar pro segundo gateway de menor taxa, quando o primeiro estiver offline")
        void teste2() {
            var transacao = criaTransacao();
            var respostaGateway = criaRespostaGateway(gatewayValorMedio, VALOR_MEDIO);

            mockGateways(transacao);
            when(gatewayMenorValor.processaPagamento(transacao)).thenThrow(GatewayOfflineException.class);
            when(gatewayValorMedio.processaPagamento(transacao)).thenReturn(respostaGateway);

            var respostaTransacaoGateway = redirecionador.processaPagamento(transacao);

            assertAll(
                    () -> assertEquals(respostaGateway.gateway().getClass(), respostaTransacaoGateway.gateway().getClass()),
                    () -> assertEquals(VALOR_MEDIO, respostaTransacaoGateway.taxa())
            );
        }

        @Test
        @DisplayName("Deve redirecionar pro terceiro gateway de menor taxa, quando o primeiro e segundo estiverem offline")
        void teste3() {
            var transacao = criaTransacao();
            var respostaGateway = criaRespostaGateway(gatewayMaiorValor, MAIOR_VALOR);

            mockGateways(transacao);
            when(gatewayMenorValor.processaPagamento(transacao)).thenThrow(GatewayOfflineException.class);
            when(gatewayValorMedio.processaPagamento(transacao)).thenThrow(GatewayOfflineException.class);
            when(gatewayMaiorValor.processaPagamento(transacao)).thenReturn(respostaGateway);

            var respostaTransacaoGateway = redirecionador.processaPagamento(transacao);

            assertAll(
                    () -> assertEquals(respostaGateway.gateway().getClass(), respostaTransacaoGateway.gateway().getClass()),
                    () -> assertEquals(MAIOR_VALOR, respostaTransacaoGateway.taxa())
            );
        }

        @Test
        @DisplayName("Deve lançar SemGatewayDisponivelException, quando todos os gateways estiverem offline")
        void teste4() {
            var transacao = criaTransacao();

            mockGateways(transacao);
            when(gatewayMenorValor.processaPagamento(transacao)).thenThrow(GatewayOfflineException.class);
            when(gatewayValorMedio.processaPagamento(transacao)).thenThrow(GatewayOfflineException.class);
            when(gatewayMaiorValor.processaPagamento(transacao)).thenThrow(GatewayOfflineException.class);

            var exception = assertThrows(SemGatewayDisponivelException.class,
                    () -> redirecionador.processaPagamento(transacao));
            assertEquals("Todos os gateways estão offline", exception.getMessage());
        }

        @Test
        @DisplayName("Não deve deixar instanciar novo RedirecionadorPagamentoOnline com uma lista de gateways vazia")
        void teste5() {
            Collection<GatewayPagamento> listaVazia = List.of();
            var ex = assertThrows(IllegalArgumentException.class,
                    () -> new RedirecionadorPagamentoOnline(listaVazia));
            assertEquals("Lista não pode estar vazia", ex.getMessage());
        }
    }

    private void mockGateways(Transacao transacao) {
        when(gatewayMenorValor.calculaTaxa(transacao.getValor())).thenReturn(MENOR_VALOR);
        when(gatewayValorMedio.calculaTaxa(transacao.getValor())).thenReturn(VALOR_MEDIO);
        when(gatewayMaiorValor.calculaTaxa(transacao.getValor())).thenReturn(MAIOR_VALOR);
    }

    private Transacao criaTransacao() {
        return new Transacao(123L,
                new Usuario("email@a.com", CARTAO_CREDITO),
                new Restaurante("Restaurante", CARTAO_CREDITO),
                BigDecimal.valueOf(1).setScale(2, HALF_UP),
                CARTAO_CREDITO,
                null,
                EM_PROCESSAMENTO);
    }

    private RespostaTransacaoGateway criaRespostaGateway(GatewayPagamento gatewayPagamento, BigDecimal taxa) {
        return new RespostaTransacaoGateway(UUID.randomUUID(), gatewayPagamento, taxa,
                LocalDateTime.now(), 123L);
    }
}