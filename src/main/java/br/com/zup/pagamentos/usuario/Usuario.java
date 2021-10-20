package br.com.zup.pagamentos.usuario;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private final Set<FormaPagamento> formasPagamento = new HashSet<>();

    /**
     * @deprecated hibernate
     */
    @Deprecated(since = "1.0")
    public Usuario() {
    }

    public Usuario(String email, FormaPagamento... formaPagamentos) {
        this.email = email;
        this.formasPagamento.addAll(Arrays.asList(formaPagamentos));
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Set<FormaPagamento> getFormasPagamento() {
        return formasPagamento;
    }
}
