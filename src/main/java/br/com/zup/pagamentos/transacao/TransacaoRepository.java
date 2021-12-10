package br.com.zup.pagamentos.transacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    boolean existsByPedidoId(Long idPedido);

    Optional<Transacao> findByPedidoIdAndStatus(Long pedidoId, StatusTransacao statusTransacao);


    Transacao findByPedidoId(Long pedidoId);

    @Query("SELECT t FROM Transacao t JOIN FETCH t.usuario JOIN FETCH t.restaurante WHERE t.id=?1 AND t.usuario.id =?2")
    Optional<Transacao> findByIdAndUsuarioIdComUsuarioERestaurante(Long transacaoId, Long usuarioId);
}