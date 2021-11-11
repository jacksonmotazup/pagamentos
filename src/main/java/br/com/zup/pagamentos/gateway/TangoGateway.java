package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.transacao.Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TangoGateway implements GatewayPagamento {
    @Override
    public BigDecimal calculaTaxa(BigDecimal valor) {
        if (valor.doubleValue() <= 100) {
            return BigDecimal.valueOf(4);
        }
        return valor.multiply(BigDecimal.valueOf(0.06));
    }

    @Override
    public Transacao processaPagamento(Transacao transacao) {
        var valor = this.calculaTaxa(transacao.getValor());
        transacao.ajustaValorAposTaxa(valor);
        transacao.conclui();
        return transacao;
    }
}
