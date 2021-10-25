package br.com.zup.pagamentos.listapagamentos;

import javax.validation.constraints.NotNull;

public record ListaPagamentosRequest(@NotNull Long usuarioId,
                                     @NotNull Long restauranteId) {

}

