package br.com.zup.pagamentos.pagamento.offline;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;
import br.com.zup.pagamentos.listapagamentos.RegraFraudeEmail;
import br.com.zup.pagamentos.pagamento.PedidoMock;
import br.com.zup.pagamentos.restaurante.RestauranteRepository;
import br.com.zup.pagamentos.transacao.Transacao;
import br.com.zup.pagamentos.usuario.UsuarioRepository;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;

import static br.com.zup.pagamentos.transacao.StatusTransacao.AGUARDANDO_CONFIRMACAO;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public record NovoPagamentoOfflineRequest(@NotNull FormaPagamento formaPagamento,
                                          @NotNull Long idRestaurante,
                                          @NotNull Long idUsuario) {

    public Transacao toTransacao(@NotNull Long idPedido, UsuarioRepository usuarioRepository, RestauranteRepository restauranteRepository) {
        var restaurante = restauranteRepository.findById(this.idRestaurante)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Restaurante não encontrado"));

        var usuario = usuarioRepository.findById(this.idUsuario)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado"));

        boolean podePagar = usuario.podePagar(restaurante, this.formaPagamento, new RegraFraudeEmail());

        if (!podePagar) {
            throw new ResponseStatusException(BAD_REQUEST, "A combinação dessa forma de pagamento entre usuário e " +
                    "restaurante não é válida");
        }
        var pedido = PedidoMock.paraPedido(idPedido);

        return new Transacao(pedido.idPedido(), usuario, restaurante, pedido.valor(), this.formaPagamento,
                null, AGUARDANDO_CONFIRMACAO);
    }
}
