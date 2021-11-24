package br.com.zup.pagamentos.pagamento;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record PedidoMock(Long idPedido,
                         BigDecimal valor) {

    public static PedidoMock paraPedido(Long idPedido) {
        return new PedidoMock(idPedido, BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP));
    }
}
