package br.com.zup.pagamentos.transacao;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;
import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.usuario.Usuario;

import javax.persistence.*;
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
    @JoinColumn(nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurante restaurante;
    @Column(nullable = false)
    private BigDecimal valor;
    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
    @Column(nullable = false)
    @Enumerated(STRING)
    private FormaPagamento formaPagamento;
    private String informacoes;
    @Enumerated(STRING)
    @Column(nullable = false)
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
