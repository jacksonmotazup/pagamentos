package br.com.zup.pagamentos.gateway;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record RespostaTransacaoGateway(UUID identificacao,
                                       GatewayPagamento gateway,
                                       BigDecimal taxa,
                                       LocalDateTime instante,
                                       Long pedidoId) {
}
