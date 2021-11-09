package br.com.zup.pagamentos.pagamento.offline;

import br.com.zup.pagamentos.listapagamentos.RegraFraudeEmail;
import br.com.zup.pagamentos.pagamento.PedidoMock;
import br.com.zup.pagamentos.restaurante.RestauranteRepository;
import br.com.zup.pagamentos.transacao.TransacaoRepository;
import br.com.zup.pagamentos.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Validated
@RestController
@RequestMapping("/api/v1/pagamento/offline")
public class PagamentoOfflineController {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final TransacaoRepository transacaoRepository;

    @Autowired
    public PagamentoOfflineController(RestauranteRepository restauranteRepository,
                                      UsuarioRepository usuarioRepository,
                                      TransacaoRepository transacaoRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @PostMapping("/{idPedido}")
    public String paga(@RequestBody @Valid NovoPagamentoOfflineRequest request,
                       @PathVariable @NotNull Long idPedido) {

        if (request.formaPagamento().isOnline()) {
            throw new ResponseStatusException(BAD_REQUEST, "Esse endpoint suporta apenas pagamentos Offline");
        }

        if (transacaoRepository.existsByIdPedido(idPedido)) {
            throw new ResponseStatusException(BAD_REQUEST, "Transação já existe");
        }

        var restaurante = restauranteRepository.findById(request.idRestaurante())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Restaurante não encontrado"));

        var usuario = usuarioRepository.findById(request.idUsuario())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado"));

        boolean podePagar = usuario.podePagar(restaurante, request.formaPagamento(), new RegraFraudeEmail());

        if (!podePagar) {
            throw new ResponseStatusException(BAD_REQUEST, "A combinação dessa forma de pagamento entre usuário e restaurante não é válida");
        }

        var pedido = PedidoMock.paraPedido(idPedido);

        var transacao = request.toTransacao(pedido, usuario, restaurante);

        transacaoRepository.save(transacao);

        return UUID.randomUUID().toString();

    }
}
