package br.com.zup.pagamentos;

import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.usuario.Usuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static br.com.zup.pagamentos.formapagamento.FormaPagamento.*;

@SpringBootApplication
public class PagamentosApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PagamentosApplication.class, args);
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    @CacheEvict(value = "pagamentos", allEntries = true)
    public void run(String... args) {
        var usuario1 = new Usuario("usuario1@email.com", CARTAO_CREDITO);
        em.persist(usuario1);
        var usuario2 = new Usuario("usuario2@email.com", CARTAO_CREDITO, MAQUINA);
        em.persist(usuario2);
        var usuario3 = new Usuario("usuario3@gmail.com", DINHEIRO, CHEQUE);
        em.persist(usuario3);
        var jackson = new Usuario("jackson@gmail.com", DINHEIRO, CHEQUE, CARTAO_CREDITO);
        em.persist(jackson);

        var restaurante1 = new Restaurante("Restaurante 1", CARTAO_CREDITO);
        em.persist(restaurante1);
        var restaurante2 = new Restaurante("Restaurante 2", CARTAO_CREDITO, MAQUINA, DINHEIRO, CHEQUE);
        em.persist(restaurante2);
    }
}
