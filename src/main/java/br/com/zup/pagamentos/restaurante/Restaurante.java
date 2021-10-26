package br.com.zup.pagamentos.restaurante;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ElementCollection
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private final Set<FormaPagamento> formasPagamentoAceitas = new HashSet<>();

    /**
     * @deprecated hibernate
     */
    @Deprecated(since = "1.0")
    public Restaurante() {
    }

    public Restaurante(String nome, FormaPagamento... formasPagamento) {
        this.nome = nome;
        this.formasPagamentoAceitas.addAll(Arrays.asList(formasPagamento));
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Set<FormaPagamento> getFormasPagamentoAceitas() {
        return formasPagamentoAceitas;
    }

    public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
        return this.formasPagamentoAceitas.contains(formaPagamento);
    }
}
