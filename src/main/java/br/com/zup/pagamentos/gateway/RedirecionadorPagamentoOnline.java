package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.compartilhado.exceptions.GatewayOfflineException;
import br.com.zup.pagamentos.transacao.Transacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
public class RedirecionadorPagamentoOnline {
    private final Collection<GatewayPagamento> gateways;

    @Autowired
    public RedirecionadorPagamentoOnline(Collection<GatewayPagamento> gateways) {
        this.gateways = gateways;
    }

    public RespostaTransacaoGateway processaPagamento(Transacao transacao) throws InterruptedException {
        var gatewaysPorOrdemMenorTaxa = this.encontraGatewayMenorTaxa(gateways, transacao.getValor());

        RespostaTransacaoGateway respostaTransacaoGateway = null;
        for (int i = 0; i < gatewaysPorOrdemMenorTaxa.size(); ) {
            try {
                respostaTransacaoGateway = gatewaysPorOrdemMenorTaxa.get(i).processaPagamento(transacao);
                break;
            } catch (GatewayOfflineException ex) {
                System.out.println("LOG: " + ex.getMessage());
                i++;
            }
        }
        return respostaTransacaoGateway;
    }

    public List<GatewayPagamento> encontraGatewayMenorTaxa(Collection<GatewayPagamento> gateways, BigDecimal valor) {
        return gateways.stream()
                .sorted(Comparator.comparing(gateway -> gateway.calculaTaxa(valor)))
                .toList();
    }
}
