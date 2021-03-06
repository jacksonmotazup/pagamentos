CREATE TABLE restaurante
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    nome VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_restaurante PRIMARY KEY (id)
);

CREATE TABLE restaurante_formas_pagamento_aceitas
(
    restaurante_id           BIGINT  NOT NULL,
    formas_pagamento_aceitas VARCHAR NOT NULL
);

ALTER TABLE restaurante_formas_pagamento_aceitas
    ADD CONSTRAINT fk_restaurante_formaspagamentoaceitas_on_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurante (id);

