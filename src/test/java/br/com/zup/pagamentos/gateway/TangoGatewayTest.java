package br.com.zup.pagamentos.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * - Gateway Tango opera na argentina, aceita todas as bandeiras e tem custo que varia entre fixo e variável.
 * Para compras de até 100 reais, o custo é fixo de 4.00. Para operações com valor maior que 100,
 * o custo vira de 6% do valor total.
 */
class TangoGatewayTest {

    private final BigDecimal VALOR_FIXO_4 = BigDecimal.valueOf(4.00).setScale(2, RoundingMode.HALF_UP);

    @Nested
    class testCalculaTaxaValorMenorOuIgualA100 {

        @Test
        @DisplayName("Deve retornar 4.00 do valor 1")
        void test1() {
            var saoriGateway = new TangoGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(1));

            assertEquals(VALOR_FIXO_4, taxa);
        }

        @Test
        @DisplayName("Deve retornar 4.00 do valor 25.06")
        void test2() {
            var saoriGateway = new TangoGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(25.06));

            assertEquals(VALOR_FIXO_4, taxa);
        }

        @Test
        @DisplayName("Deve retornar 4.00 do valor 75.89")
        void test3() {
            var saoriGateway = new TangoGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(75.89));

            assertEquals(VALOR_FIXO_4, taxa);
        }

        @Test
        @DisplayName("Deve retornar 4.00 do valor 0")
        void test4() {
            var saoriGateway = new TangoGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(0));

            assertEquals(VALOR_FIXO_4, taxa);
        }

        @Test
        @DisplayName("Deve retornar 4.00 do valor 100")
        void test5() {
            var saoriGateway = new TangoGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(100));

            assertEquals(VALOR_FIXO_4, taxa);
        }
    }

    @Nested
    class testCalculaTaxaValorMaiorQue100 {

        @Test
        @DisplayName("Deve retornar 6% do valor 100.01")
        void test1() {
            var saoriGateway = new TangoGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(101.01));

            assertEquals(BigDecimal.valueOf(6.06), taxa);
        }

        @Test
        @DisplayName("Deve retornar 6% do valor 200.00")
        void test2() {
            var saoriGateway = new TangoGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(200.00));

            assertEquals(BigDecimal.valueOf(12.00).setScale(2, RoundingMode.HALF_UP), taxa);
        }
    }

}