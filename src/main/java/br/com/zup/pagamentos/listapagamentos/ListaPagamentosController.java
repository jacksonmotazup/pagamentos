package br.com.zup.pagamentos.listapagamentos;

import br.com.zup.pagamentos.restaurante.RestauranteRepository;
import br.com.zup.pagamentos.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/pagamentos")
public class ListaPagamentosController {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final RegraFraude regraFraude;

    @Autowired
    public ListaPagamentosController(RestauranteRepository restauranteRepository,
                                     UsuarioRepository usuarioRepository,
                                     RegraFraude regraFraude) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
        this.regraFraude = regraFraude;
    }

    @GetMapping
//    @Cacheable(value = "pagamentos")
    public PagamentosResponse listaPagamentosUsuarioRestaurante(@Valid @RequestBody ListaPagamentosRequest request) {
        var restaurante = restauranteRepository.findById(request.restauranteId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Restaurante não encontrado"));

        var usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado"));

        var formasPagamentosComum = usuario.formasAceitas(restaurante, regraFraude);

        return new PagamentosResponse(formasPagamentosComum);
    }
}
