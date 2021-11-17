package br.com.zup.pagamentos.transacao;

import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.usuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static br.com.zup.pagamentos.formapagamento.FormaPagamento.CARTAO_CREDITO;
import static br.com.zup.pagamentos.transacao.StatusTransacao.CONCLUIDA;
import static br.com.zup.pagamentos.transacao.StatusTransacao.EM_PROCESSAMENTO;
import static java.math.RoundingMode.HALF_UP;
import static org.junit.jupiter.api.Assertions.*;

class TransacaoTest {

    private Transacao criaTransacao() {
        return new Transacao(123L,
                new Usuario("email@a.com", CARTAO_CREDITO),
                new Restaurante("Restaurante", CARTAO_CREDITO),
                BigDecimal.valueOf((double) 100),
                CARTAO_CREDITO,
                null,
                EM_PROCESSAMENTO);
    }

    @Test
    @DisplayName("Não deve aceitar valor de taxa nulo ao concluir transação online")
    void teste1() {

        var transacao = this.criaTransacao();

        var ex = assertThrows(IllegalArgumentException.class,
                () -> transacao.concluiTransacaoOnline(null));
        assertEquals("Taxa não pode ser nula", ex.getMessage());
    }

    @Test
    @DisplayName("Não deve aceitar valor de taxa menor que zero ao concluir transação online")
    void teste2() {

        var transacao = this.criaTransacao();
        var valorNegativo = BigDecimal.valueOf(-1);

        var ex = assertThrows(IllegalStateException.class,
                () -> transacao.concluiTransacaoOnline(valorNegativo));
        assertEquals("Valor da taxa deve ser maior que zero", ex.getMessage());
    }

    @Test
    @DisplayName("Não deve aceitar valor de taxa igual a zero ao concluir transação online")
    void teste3() {

        var transacao = this.criaTransacao();

        var ex = assertThrows(IllegalStateException.class,
                () -> transacao.concluiTransacaoOnline(BigDecimal.ZERO));
        assertEquals("Valor da taxa deve ser maior que zero", ex.getMessage());
    }

    @Test
    @DisplayName("Deve aceitar valor de taxa maior que zero ao concluir transação online")
    void teste4() {
        var transacao = this.criaTransacao();

        assertAll(
                () -> assertDoesNotThrow(() -> transacao.concluiTransacaoOnline(BigDecimal.valueOf(1))),
                () -> assertEquals(BigDecimal.valueOf(99.00).setScale(2, HALF_UP), transacao.getValor()),
                () -> assertEquals(CONCLUIDA, transacao.getStatus())
        );


    }
}