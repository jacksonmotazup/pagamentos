package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.transacao.Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SaoriGateway implements GatewayPagamento {
    @Override
    public BigDecimal calculaTaxa(BigDecimal valor) {
        return valor.multiply(BigDecimal.valueOf(0.05));
    }

    @Override
    public Transacao processaPagamento(Transacao transacao) {
        var valor = this.calculaTaxa(transacao.getValor());
        transacao.ajustaValorAposTaxa(valor);
        transacao.conclui();
        return transacao;
    }

}
