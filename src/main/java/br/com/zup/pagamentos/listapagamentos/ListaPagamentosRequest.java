package br.com.zup.pagamentos.listapagamentos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record ListaPagamentosRequest(@NotNull @Positive Long usuarioId,
                                     @NotNull @Positive Long restauranteId) {

}

