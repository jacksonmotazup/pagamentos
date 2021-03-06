insert into usuario (email)
select left(md5(random()::text), 6) || '@zup.com.br' as email

from generate_series(1, 1000000) s(i);



insert into restaurante(nome)
select 'restaurante.' || left(md5(random()::text), 3) as nome

from generate_series(1, 1000000) s(i);


insert into usuario_formas_pagamento (usuario_id, formas_pagamento)
with formas_pag AS (
    select *
    from (values ('CARTAO_CREDITO'), ('DINHEIRO'), ('CHEQUE')) as t(forma_pagamento)
)
select u.id,
       f.forma_pagamento
from usuario u,
     formas_pag f
;



insert into restaurante_formas_pagamento_aceitas (restaurante_id, formas_pagamento_aceitas)
with formas_pag AS (
    select *
    from (values ('CARTAO_CREDITO'), ('DINHEIRO'), ('MAQUINA')) as t(forma_pagamento)
)
select r.id,
       f.forma_pagamento
from restaurante r,
     formas_pag f
;

insert into transacao (pedido_id, usuario_id, restaurante_id, valor, data_criacao, forma_pagamento, informacoes, status)
select i                as pedido_id
     , 1                as usuario_id
     , 1                as restaurante_id
     , 100              as valor
     , localtimestamp   as data_criacao
     , 'CARTAO_CREDITO' as forma_pagamento
     , null             as informacoes
     , 'CONCLUIDA'      as status

from generate_series(-1000000, -1) s(i);

