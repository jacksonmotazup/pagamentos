package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.transacao.Transacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;

@Service
public class RedirecionadorGateways {
    private final Collection<GatewayPagamento> gateways;

    @Autowired
    public RedirecionadorGateways(Collection<GatewayPagamento> gateways) {
        this.gateways = gateways;
    }

    public Transacao processaPagamento(Transacao transacao) {
        var gatewayEscolhido = this.retornaGatewayMenorTaxa(gateways, transacao.getValor());

        return gatewayEscolhido.processaPagamento(transacao);

    }

    private GatewayPagamento retornaGatewayMenorTaxa(Collection<GatewayPagamento> gateways, BigDecimal valor) {
        return gateways.stream()
                .min(Comparator.comparing(gateway -> gateway.calculaTaxa(valor)))
                .orElseThrow(() -> new RuntimeException("Erro ao recuperar Gateway"));
    }
}
