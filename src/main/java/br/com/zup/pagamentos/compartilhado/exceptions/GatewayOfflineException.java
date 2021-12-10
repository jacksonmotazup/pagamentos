package br.com.zup.pagamentos.compartilhado.exceptions;

public class GatewayOfflineException extends RuntimeException {
    public GatewayOfflineException(String mensagem) {
        super(mensagem);
    }
}
