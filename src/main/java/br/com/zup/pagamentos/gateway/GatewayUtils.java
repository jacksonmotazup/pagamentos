package br.com.zup.pagamentos.gateway;

import br.com.zup.pagamentos.compartilhado.exceptions.GatewayOfflineException;

import java.util.Random;

public class GatewayUtils {

    private final Random random = new Random();

    public void simulaException(String classe) {
        var simulaPossivelException = this.random.nextInt(1, 10);
        if (simulaPossivelException == 1) {
            throw new GatewayOfflineException("Gateway " + classe + " está offline.");
        }
    }

    public void congelaThread(int de, int ate) {
        var tempo = this.random.nextInt(de, ate);

        try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            System.out.println("LOG: " + e.getMessage());
        }
    }

}
