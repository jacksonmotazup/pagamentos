package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.compartilhado.exceptions.GatewayOfflineException;
import br.com.zup.pagamentos.compartilhado.exceptions.SemGatewayDisponivelException;
import br.com.zup.pagamentos.transacao.Transacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@Validated
public class RedirecionadorPagamentoOnline {
    @NotEmpty
    private final Collection<GatewayPagamento> gateways;

    @Autowired
    public RedirecionadorPagamentoOnline(@NotEmpty Collection<GatewayPagamento> gateways) {
        this.gateways = gateways;
    }

    public RespostaTransacaoGateway processaPagamento(Transacao transacao) {
        var gatewaysPorOrdemMenorTaxa = this.encontraGatewayMenorTaxa(gateways, transacao.getValor());

        RespostaTransacaoGateway respostaTransacaoGateway = null;
        for (int i = 0; i <= gatewaysPorOrdemMenorTaxa.size(); ) {
            try {
                if (i == gatewaysPorOrdemMenorTaxa.size()) {
                    throw new SemGatewayDisponivelException("Todos os gateways estão offline");
                }
                respostaTransacaoGateway = gatewaysPorOrdemMenorTaxa.get(i).processaPagamento(transacao);
                break;
            } catch (GatewayOfflineException ex) {
                System.out.println("LOG: " + ex.getMessage());
                i++;
            }
        }
        return respostaTransacaoGateway;
    }

    private List<GatewayPagamento> encontraGatewayMenorTaxa(Collection<GatewayPagamento> gateways, BigDecimal valor) {
        return gateways.stream()
                .sorted(Comparator.comparing(gateway -> gateway.calculaTaxa(valor)))
                .toList();
    }
}
