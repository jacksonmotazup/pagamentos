package br.com.zup.pagamentos.listapagamentos;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;

import java.util.List;

public record PagamentosResponse(List<FormaPagamento> formasPagamento) {
}
