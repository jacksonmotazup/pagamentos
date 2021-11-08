create table restaurante
(
    id   bigserial    not null,
    nome varchar(255) not null,
    primary key (id)
);

create table restaurante_formas_pagamento_aceitas
(
    restaurante_id           int8         not null,
    formas_pagamento_aceitas varchar(255) not null,
    primary key (restaurante_id, formas_pagamento_aceitas)
);

alter table restaurante_formas_pagamento_aceitas
    add constraint FK_RESTAURANTE_ID
        foreign key (restaurante_id)
            references restaurante;

