package br.com.zup.pagamentos.transacao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    boolean existsByIdPedido(Long idPedido);
}