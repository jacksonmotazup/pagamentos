package br.com.zup.pagamentos.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
  * - Gateway Saori opera no brasil, só aceita visa e master, tem taxa percentual de 5% sobre a compra
 */
class SaoriGatewayTest {

    @Nested
    class testCalculaTaxa {

        @Test
        @DisplayName("Deve retornar 5% do valor 1 calculado e arredondado")
        void test1() {
            var saoriGateway = new SaoriGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(1));

            assertEquals(BigDecimal.valueOf(0.05), taxa);
        }

        @Test
        @DisplayName("Deve retornar 5% do valor 125.06 calculado e arredondado")
        void test2() {
            var saoriGateway = new SaoriGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(125.06));

            assertEquals(BigDecimal.valueOf(6.25), taxa);
        }

        @Test
        @DisplayName("Deve retornar 5% do valor 1225.89 calculado e arredondado")
        void test3() {
            var saoriGateway = new SaoriGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(1225.89));

            assertEquals(BigDecimal.valueOf(61.29), taxa);
        }

        @Test
        @DisplayName("Deve retornar 5% do valor 0 calculado e arredondado")
        void test4() {
            var saoriGateway = new SaoriGateway();

            var taxa = saoriGateway.calculaTaxa(BigDecimal.valueOf(0));

            assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP), taxa);
        }
    }

}