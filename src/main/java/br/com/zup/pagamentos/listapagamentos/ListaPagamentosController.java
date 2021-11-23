package br.com.zup.pagamentos.listapagamentos;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/v1/pagamentos")
public class ListaPagamentosController {

    private final EntityManager entityManager;

    @Autowired
    public ListaPagamentosController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @GetMapping
    @Cacheable(value = "pagamentos")
    public PagamentosResponse listaPagamentosUsuarioRestaurante(@Valid @RequestBody ListaPagamentosRequest request) {
        var list = (List<FormaPagamento>) entityManager
                .createNativeQuery("select u.formas_pagamento " +
                        "from usuario_formas_pagamento u, restaurante_formas_pagamento_aceitas r " +
                        "where usuario_id =?1 " +
                        "and restaurante_id =?2 " +
                        "and u.formas_pagamento=r.formas_pagamento_aceitas")
                .setParameter(1, request.usuarioId())
                .setParameter(2, request.restauranteId())
                .getResultStream().map(f -> FormaPagamento.valueOf(f.toString())).toList();

        if (list.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Formas de pagamento não encontradas");
        }

        return new PagamentosResponse(list);
    }
}
