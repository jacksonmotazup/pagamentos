package br.com.zup.pagamentos.listapagamentos;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;
import br.com.zup.pagamentos.usuario.Usuario;

public interface RegraFraude {

    boolean aceita(Usuario usuario, FormaPagamento forma);
}
