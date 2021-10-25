package br.com.zup.pagamentos.listapagamentos;

import br.com.zup.pagamentos.restaurante.RestauranteRepository;
import br.com.zup.pagamentos.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pagamentos")
public class ListaPagamentosController {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ListaPagamentosController(RestauranteRepository restauranteRepository, UsuarioRepository usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }


    @GetMapping
    public List<PagamentosResponse> listaPagamentosUsuarioRestaurante(@Valid @RequestBody ListaPagamentosRequest request) {
        var restaurante = restauranteRepository.findById(request.restauranteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));

        var usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        var formasPagamentosComum = usuario.formasAceitas(restaurante);

        return formasPagamentosComum.stream().map(PagamentosResponse::new).toList();
    }


}
