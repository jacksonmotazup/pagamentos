package br.com.zup.pagamentos.compartilhado.exceptions;

public class SemGatewayDisponivelException extends RuntimeException {
    public SemGatewayDisponivelException(String mensagem) {
        super(mensagem);
    }
}
