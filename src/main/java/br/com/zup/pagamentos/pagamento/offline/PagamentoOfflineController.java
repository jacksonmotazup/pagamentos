package br.com.zup.pagamentos.pagamento.offline;

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

        var transacao = request.toTransacao(idPedido, usuarioRepository, restauranteRepository);

        transacaoRepository.save(transacao);

        return UUID.randomUUID().toString();

    }
}
