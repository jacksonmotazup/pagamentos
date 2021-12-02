package br.com.zup.pagamentos.pagamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public record PedidoMock(Long idPedido,
                         BigDecimal valor) {

    public static PedidoMock paraPedido(Long idPedido){
        var random = new Random();
        var tempo = random.nextInt(5, 20);

        try {
            Thread.sleep(tempo);
        }catch (InterruptedException ex){
            System.out.println(ex.getMessage());
        }

        var valor = random.nextInt(1, 2000);
        return new PedidoMock(idPedido, BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP));
    }
}
