package br.com.zup.pagamentos.formapagamento;

public enum FormaPagamento {

    CARTAO_MASTER(true, "Cartão MasterCard"),
    CARTAO_VISA(true, "Cartão Visa"),
    DINHEIRO(false, "Dinheiro presencialmente"),
    MAQUINA(false, "Maquina de cartão presencialmente"),
    CHEQUE(false, "Cheque presencialmente");

    private final boolean online;
    private final String descricao;

    FormaPagamento(boolean online, String descricao) {
        this.online = online;
        this.descricao = descricao;
    }

    public boolean isOnline() {
        return online;
    }

    public String getDescricao() {
        return descricao;
    }
}
