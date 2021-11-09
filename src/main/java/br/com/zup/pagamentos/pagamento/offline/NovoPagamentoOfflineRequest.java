package br.com.zup.pagamentos.pagamento.offline;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;
import br.com.zup.pagamentos.pagamento.PedidoMock;
import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.transacao.Transacao;
import br.com.zup.pagamentos.usuario.Usuario;

import javax.validation.constraints.NotNull;

import static br.com.zup.pagamentos.transacao.StatusTransacao.AGUARDANDO_CONFIRMACAO;

public record NovoPagamentoOfflineRequest(@NotNull FormaPagamento formaPagamento,
                                          @NotNull Long idRestaurante,
                                          @NotNull Long idUsuario) {

    public Transacao toTransacao(PedidoMock pedido, Usuario usuario, Restaurante restaurante) {
        return new Transacao(pedido.idPedido(), usuario, restaurante, pedido.valor(), this.formaPagamento,
                null, AGUARDANDO_CONFIRMACAO);
    }
}
