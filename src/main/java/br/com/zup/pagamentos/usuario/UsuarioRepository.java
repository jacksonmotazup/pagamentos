package br.com.zup.pagamentos.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u JOIN FETCH u.formasPagamento WHERE u.id=?1")
    Optional<Usuario> findByIdComFormasPagamento(Long usuarioId);
}