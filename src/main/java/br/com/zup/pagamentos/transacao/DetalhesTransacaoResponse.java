package br.com.zup.pagamentos.transacao;

import br.com.zup.pagamentos.formapagamento.FormaPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DetalhesTransacaoResponse {
    private Long id;
    private Long pedidoId;
    private String emailUsuario;
    private String nomeRestaurante;
    private BigDecimal valor;
    private LocalDateTime dataCriacao;
    private FormaPagamento formaPagamento;
    private String informacoes;
    private StatusTransacao status;

    public DetalhesTransacaoResponse(Transacao transacao) {
        this.id = transacao.getId();
        this.pedidoId = transacao.getPedidoId();
        this.emailUsuario = transacao.getUsuario().getEmail();
        this.nomeRestaurante = transacao.getRestaurante().getNome();
        this.valor = transacao.getValor();
        this.dataCriacao = transacao.getDataCriacao();
        this.formaPagamento = transacao.getFormaPagamento();
        this.informacoes = transacao.getInformacoes();
        status = transacao.getStatus();
    }

    public DetalhesTransacaoResponse() {
    }

    public Long getId() {
        return id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public String getNomeRestaurante() {
        return nomeRestaurante;
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
