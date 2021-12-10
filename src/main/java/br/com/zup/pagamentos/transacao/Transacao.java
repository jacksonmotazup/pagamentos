package br.com.zup.pagamentos.transacao;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;
import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.usuario.Usuario;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static br.com.zup.pagamentos.transacao.StatusTransacao.CONCLUIDA;
import static br.com.zup.pagamentos.transacao.StatusTransacao.ERRO_PROCESSAMENTO;
import static javax.persistence.EnumType.STRING;

@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long pedidoId;
    @ManyToOne
    @NotNull
    private Usuario usuario;
    @ManyToOne
    @NotNull
    private Restaurante restaurante;
    private BigDecimal valor;
    @NotNull
    private final LocalDateTime dataCriacao = LocalDateTime.now();
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

    public Long getPedidoId() {
        return pedidoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public StatusTransacao getStatus() {
        return status;
    }

    public String getInformacoes() {
        return informacoes;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void concluiTransacaoOffline() {
        this.status = CONCLUIDA;
    }

    public boolean isPagamentoOnline() {
        return this.formaPagamento.isOnline();
    }

    public void concluiTransacaoOnline(BigDecimal taxa) {
        Assert.notNull(taxa, "Taxa não pode ser nula");
        Assert.state(taxa.compareTo(BigDecimal.ZERO) > 0, "Valor da taxa deve ser maior que zero");

        this.valor = this.valor.subtract(taxa.setScale(2, RoundingMode.HALF_UP));
        this.status = CONCLUIDA;
    }

    public void marcaComoErro() {
        this.status = ERRO_PROCESSAMENTO;
    }

    public void adicionaValor(BigDecimal valor) {
        this.valor = valor;
    }
}
