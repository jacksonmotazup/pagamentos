-- cria index para USUARIO_FORMAS_PAGAMENTO.usuario_id
CREATE INDEX usuario_formas_pag_usuario_id_idx
    ON public.usuario_formas_pagamento USING btree (usuario_id);

-- cria index para RESTAURANTE_FORMAS_PAGAMENTO_ACEITAS.restaurante_id
CREATE INDEX restaurante_formas_pag_restaurante_id_idx
    ON public.restaurante_formas_pagamento_aceitas USING btree (restaurante_id);