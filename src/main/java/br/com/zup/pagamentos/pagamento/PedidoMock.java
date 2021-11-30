package br.com.zup.pagamentos.pagamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public record PedidoMock(Long idPedido,
                         BigDecimal valor) {

    public static PedidoMock paraPedido(Long idPedido) throws InterruptedException {
        var random = new Random();

        var tempo = random.nextInt(5, 20);
        Thread.sleep(tempo);

        var valor = random.nextInt(1, 2000);
        return new PedidoMock(idPedido, BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP));
    }
}
