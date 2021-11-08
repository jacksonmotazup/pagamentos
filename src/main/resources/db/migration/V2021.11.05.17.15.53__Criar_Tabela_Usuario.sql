create table usuario
(
    id    bigserial    not null,
    email varchar(255) not null,
    primary key (id)
);

create table usuario_formas_pagamento
(
    usuario_id       int8         not null,
    formas_pagamento varchar(255) not null,
    primary key (usuario_id, formas_pagamento)
);

alter table usuario_formas_pagamento
    add constraint FK_USUARIO_ID
        foreign key (usuario_id)
            references usuario;