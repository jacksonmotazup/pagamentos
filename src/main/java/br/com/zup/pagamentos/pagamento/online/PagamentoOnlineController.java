package br.com.zup.pagamentos.pagamento.online;

import br.com.zup.pagamentos.restaurante.RestauranteRepository;
import br.com.zup.pagamentos.transacao.TransacaoRepository;
import br.com.zup.pagamentos.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/v1/pagamento/online")
public class PagamentoOnlineController {

    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RestauranteRepository restauranteRepository;
    private final ProcessadorPagamentoOnline processadorPagamento;

    @Autowired
    public PagamentoOnlineController(TransacaoRepository transacaoRepository,
                                     UsuarioRepository usuarioRepository,
                                     RestauranteRepository restauranteRepository,
                                     ProcessadorPagamentoOnline processadorPagamento) {
        this.transacaoRepository = transacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
        this.processadorPagamento = processadorPagamento;
    }

    @PostMapping("/{pedidoId}")
    public void paga(@PathVariable Long pedidoId,
                     @RequestBody @Valid NovoPagamentoOnlineRequest request) {
        if (request.isPagamentoOffline()) {
            throw new ResponseStatusException(BAD_REQUEST, "Esse endpoint suporta apenas pagamentos Online");
        }
        if (transacaoRepository.existsByPedidoId(pedidoId)) {
            throw new ResponseStatusException(BAD_REQUEST, "Transação já existe");
        }

        var novaTransacao = request.paraTransacao(pedidoId, usuarioRepository, restauranteRepository);
        transacaoRepository.save(novaTransacao);

        processadorPagamento.processaPagamento(novaTransacao.getId(), pedidoId);
    }
}
