package br.com.zup.pagamentos.listapagamentos;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;
import br.com.zup.pagamentos.usuario.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static br.com.zup.pagamentos.formapagamento.FormaPagamento.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegraFraudeEmailTest {

    private final RegraFraudeEmail regraFraudeEmail = new RegraFraudeEmail();

    @Test
    @DisplayName("Deve retornar TRUE quando a forma é OFFLINE e email não começa com J")
    void teste1() {
        var usuario = new Usuario("email@a.com", DINHEIRO, CHEQUE, MAQUINA);

        usuario.getFormasPagamento().forEach(forma -> assertTrue(regraFraudeEmail.aceita(usuario, forma)));
    }

    @Test
    @DisplayName("Deve retornar TRUE quando a forma é OFFLINE e email começa com J")
    void teste2() {
        var usuario = new Usuario("jemail@a.com", DINHEIRO, CHEQUE, MAQUINA);

        usuario.getFormasPagamento().forEach(forma -> assertTrue(regraFraudeEmail.aceita(usuario, forma)));
    }

    @Test
    @DisplayName("Deve retornar TRUE quando a forma é ONLINE e email não começa com J")
    void teste3() {
        var usuario = new Usuario("email@a.com", CARTAO_CREDITO);

        usuario.getFormasPagamento().forEach(forma -> assertTrue(regraFraudeEmail.aceita(usuario, forma)));
    }

    @Test
    @DisplayName("Deve retornar FALSE quando a forma é ONLINE e email  começa com J")
    void teste4() {
        var usuario = new Usuario("jemail@a.com", CARTAO_CREDITO);

        usuario.getFormasPagamento().forEach(forma -> assertFalse(regraFraudeEmail.aceita(usuario, forma)));
    }
}