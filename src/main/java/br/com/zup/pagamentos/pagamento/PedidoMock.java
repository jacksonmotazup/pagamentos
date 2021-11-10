package br.com.zup.pagamentos.pagamento;

import java.math.BigDecimal;
import java.util.Random;

public record PedidoMock(Long idPedido,
                         BigDecimal valor) {

    public static PedidoMock paraPedido(Long idPedido) {
        var random = new Random();
        var numero = random.nextDouble(100);
        return new PedidoMock(idPedido, BigDecimal.valueOf(numero));
    }
}
