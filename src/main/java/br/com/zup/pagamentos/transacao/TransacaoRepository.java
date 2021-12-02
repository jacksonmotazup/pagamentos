package br.com.zup.pagamentos.transacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    boolean existsByPedidoId(Long idPedido);

    Optional<Transacao> findByPedidoIdAndStatus(Long pedidoId, StatusTransacao statusTransacao);


    Transacao findByPedidoId(Long pedidoId);
}