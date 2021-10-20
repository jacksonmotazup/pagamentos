package br.com.zup.pagamentos.formapagamento;

public enum FormaPagamento {

    CARTAO_CREDITO(true, "Cartão"),
    DINHEIRO(false, "Dinheiro"),
    MAQUINA(false, "Maquina de cartão"),
    CHEQUE(false, "Cheque");

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
