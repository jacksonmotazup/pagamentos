package br.com.zup.pagamentos.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * - Gateway Seya opera no brasil, aceita todas bandeiras, tem custo fixo de 6.00.

 */
class SeyaGatewayTest {

    private final BigDecimal VALOR_FIXO_6 = BigDecimal.valueOf(6.00).setScale(2, RoundingMode.HALF_UP);

    @Nested
    class testCalculaTaxa {

        @Test
        @DisplayName("Deve retornar 6.00 do valor 1")
        void test1() {
            var saoriGateway = new SeyaGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(1));

            assertEquals(VALOR_FIXO_6, taxa);
        }

        @Test
        @DisplayName("Deve retornar 6.00 do valor 125.06")
        void test2() {
            var saoriGateway = new SeyaGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(125.06));

            assertEquals(VALOR_FIXO_6, taxa);
        }

        @Test
        @DisplayName("Deve retornar 6.00 do valor 1225.89")
        void test3() {
            var saoriGateway = new SeyaGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(1225.89));

            assertEquals(VALOR_FIXO_6, taxa);
        }

        @Test
        @DisplayName("Deve retornar 6.00 do valor 0")
        void test4() {
            var saoriGateway = new SeyaGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(0));

            assertEquals(VALOR_FIXO_6, taxa);
        }
    }

}