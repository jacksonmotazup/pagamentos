package br.com.zup.pagamentos.transacao;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;
import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.usuario.Usuario;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;

@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private Long pedidoId;
    @ManyToOne
    @NotNull
    private Usuario usuario;
    @ManyToOne
    @NotNull
    private Restaurante restaurante;
    @NotNull
    private BigDecimal valor;
    @NotNull
    private LocalDateTime dataCriacao = LocalDateTime.now();
    @NotNull
    @Enumerated(STRING)
    private FormaPagamento formaPagamento;
    private String informacoes;
    @Enumerated(STRING)
    @NotNull
    private StatusTransacao status;

    @Deprecated
    public Transacao() {
    }

    public Transacao(Long pedidoId, Usuario usuario, Restaurante restaurante, BigDecimal valor,
                     FormaPagamento formaPagamento, String informacoes,
                     StatusTransacao status) {
        this.pedidoId = pedidoId;
        this.usuario = usuario;
        this.restaurante = restaurante;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.informacoes = informacoes;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public String getInformacoes() {
        return informacoes;
    }

    public StatusTransacao getStatus() {
        return status;
    }
}
