CREATE TABLE transacao
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pedido_id       BIGINT                                  NOT NULL,
    usuario_id      BIGINT                                  NOT NULL,
    restaurante_id  BIGINT                                  NOT NULL,
    valor           DECIMAL                                 NOT NULL,
    data_criacao    TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    forma_pagamento VARCHAR(30)                            NOT NULL,
    informacoes     VARCHAR(255),
    status          VARCHAR(30)                            NOT NULL,
    CONSTRAINT pk_transacao PRIMARY KEY (id)
);

ALTER TABLE transacao
    ADD CONSTRAINT uc_transacao_pedidoid UNIQUE (pedido_id);

ALTER TABLE transacao
    ADD CONSTRAINT FK_TRANSACAO_ON_RESTAURANTE FOREIGN KEY (restaurante_id) REFERENCES restaurante (id);

ALTER TABLE transacao
    ADD CONSTRAINT FK_TRANSACAO_ON_USUARIO FOREIGN KEY (usuario_id) REFERENCES usuario (id);
