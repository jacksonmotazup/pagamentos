package br.com.zup.pagamentos.listapagamentos;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;
import br.com.zup.pagamentos.usuario.Usuario;
import org.springframework.stereotype.Service;

@Service
public class RegraFraudeEmail implements RegraFraude {

    @Override
    public boolean aceita(Usuario usuario, FormaPagamento forma) {
        if (!forma.isOnline()) {
            return true;
        }
        return !usuario.getEmail().startsWith("j");
    }
}
