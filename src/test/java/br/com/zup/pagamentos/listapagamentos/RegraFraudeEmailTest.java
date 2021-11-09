package br.com.zup.pagamentos.listapagamentos;

import br.com.zup.pagamentos.usuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static br.com.zup.pagamentos.formapagamento.FormaPagamento.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegraFraudeEmailTest {

    private final RegraFraudeEmail regraFraudeEmail = new RegraFraudeEmail();

    @Test
    @DisplayName("Deve aceitar quando a forma é OFFLINE e email não começa com J")
    void teste1() {
        var usuario = new Usuario("email@a.com", DINHEIRO);

        assertTrue(regraFraudeEmail.aceita(usuario, DINHEIRO));
        assertTrue(regraFraudeEmail.aceita(usuario, CHEQUE));
        assertTrue(regraFraudeEmail.aceita(usuario, MAQUINA));
    }

    @Test
    @DisplayName("Deve aceitar quando a forma é OFFLINE e email começa com J")
    void teste2() {
        var usuario = new Usuario("jemail@a.com", DINHEIRO);

        assertTrue(regraFraudeEmail.aceita(usuario, DINHEIRO));
        assertTrue(regraFraudeEmail.aceita(usuario, CHEQUE));
        assertTrue(regraFraudeEmail.aceita(usuario, MAQUINA));
    }

    @Test
    @DisplayName("Deve aceitar quando a forma é ONLINE e email não começa com J")
    void teste3() {
        var usuario = new Usuario("email@a.com", CARTAO_CREDITO);

        assertTrue(regraFraudeEmail.aceita(usuario, CARTAO_CREDITO));
    }

    @Test
    @DisplayName("Não deve aceitar quando a forma é ONLINE e email  começa com J")
    void teste4() {
        var usuario = new Usuario("jemail@a.com", CARTAO_CREDITO);

        assertFalse(regraFraudeEmail.aceita(usuario, CARTAO_CREDITO));
    }
}