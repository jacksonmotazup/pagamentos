package br.com.zup.pagamentos.transacao;

import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.usuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static br.com.zup.pagamentos.formapagamento.FormaPagamento.CARTAO_CREDITO;
import static br.com.zup.pagamentos.transacao.StatusTransacao.EM_PROCESSAMENTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        var ex = assertThrows(IllegalStateException.class,
                () -> transacao.concluiTransacaoOnline(BigDecimal.valueOf(-1)));
        assertEquals("Valor da taxa deve ser maior que zero", ex.getMessage());
    }

    @Test
    @DisplayName("Não deve aceitar valor de taxa igual a zero ao concluir transação online")
    void teste3() {

        var transacao = this.criaTransacao();

        var ex = assertThrows(IllegalStateException.class,
                () -> transacao.concluiTransacaoOnline(BigDecimal.valueOf(0)));
        assertEquals("Valor da taxa deve ser maior que zero", ex.getMessage());
    }
}