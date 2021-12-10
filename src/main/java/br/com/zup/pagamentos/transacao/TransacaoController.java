package br.com.zup.pagamentos.transacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/transacoes")
public class TransacaoController {

    private final TransacaoRepository transacaoRepository;

    @Autowired
    public TransacaoController(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    @GetMapping
    public DetalhesTransacaoResponse detalhesTransacao(@Valid DetalhesTransacaoRequest request) {
        var transacao = transacaoRepository.findByIdAndUsuarioIdComUsuarioERestaurante(request.transacaoId(),
                        request.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Transação não encontrada"));

        return new DetalhesTransacaoResponse(transacao);
    }
}
