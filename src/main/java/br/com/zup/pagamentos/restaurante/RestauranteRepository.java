package br.com.zup.pagamentos.restaurante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    @Query("SELECT r FROM Restaurante r JOIN FETCH r.formasPagamentoAceitas WHERE r.id=?1")
    Optional<Restaurante> findByIdComFormasPagamento(Long restauranteId);
}