package br.com.zup.pagamentos.usuario;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private final List<FormaPagamento> formasPagamento = new ArrayList<>();

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

    public List<FormaPagamento> getFormasPagamento() {
        return formasPagamento;
    }
}
