package br.com.zup.pagamentos.pagamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public record PedidoMock(Long idPedido,
                         BigDecimal valor) {

    public static PedidoMock paraPedido(Long idPedido) throws InterruptedException {
        var tempo = new Random().nextInt(5, 20);
        Thread.sleep(tempo);
        return new PedidoMock(idPedido, BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP));
    }
}
