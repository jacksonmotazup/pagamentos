package br.com.zup.pagamentos.pagamento.offline;

import br.com.zup.pagamentos.restaurante.RestauranteRepository;
import br.com.zup.pagamentos.transacao.TransacaoRepository;
import br.com.zup.pagamentos.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static br.com.zup.pagamentos.transacao.StatusTransacao.AGUARDANDO_CONFIRMACAO;
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

    @PostMapping("/{pedidoId}")
    @Transactional
    public Long paga(@RequestBody @Valid NovoPagamentoOfflineRequest request,
                     @PathVariable @NotNull Long pedidoId) {

        if (request.isPagamentoOnline()) {
            throw new ResponseStatusException(BAD_REQUEST, "Esse endpoint suporta apenas pagamentos Offline");
        }

        if (transacaoRepository.existsByPedidoId(pedidoId)) {
            throw new ResponseStatusException(BAD_REQUEST, "Transação já existe");
        }

        var transacao = request.toTransacao(pedidoId, usuarioRepository, restauranteRepository);

        transacaoRepository.save(transacao);

        return transacao.getId();
    }

    @PatchMapping("/{pedidoId}")
    @Transactional
    public String concluiPagamentoOffline(@PathVariable Long pedidoId) {
        var transacao = transacaoRepository.findByPedidoIdAndStatus(pedidoId, AGUARDANDO_CONFIRMACAO)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Transação não encontrada"));

        if (transacao.getFormaPagamento().isOnline()) {
            throw new ResponseStatusException(BAD_REQUEST, "Esse endpoint suporta apenas pagamentos Offline");
        }

        transacao.conclui();

        return "Transação concluída";
    }
}
