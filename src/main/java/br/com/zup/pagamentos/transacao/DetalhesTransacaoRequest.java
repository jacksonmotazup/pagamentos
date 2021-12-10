package br.com.zup.pagamentos.transacao;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

record DetalhesTransacaoRequest(@Positive @NotNull Long transacaoId,
                                @Positive @NotNull Long usuarioId) {
}
