package br.com.zup.pagamentos.pagamento.online;

import br.com.zup.pagamentos.compartilhado.handler.ExceptionHandlerResponse;
import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.restaurante.RestauranteRepository;
import br.com.zup.pagamentos.transacao.TransacaoRepository;
import br.com.zup.pagamentos.usuario.Usuario;
import br.com.zup.pagamentos.usuario.UsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.zup.pagamentos.formapagamento.FormaPagamento.CARTAO_CREDITO;
import static br.com.zup.pagamentos.formapagamento.FormaPagamento.DINHEIRO;
import static br.com.zup.pagamentos.transacao.StatusTransacao.CONCLUIDA;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PagamentoOnlineControllerTest {

    private final Long PEDIDO_ID = 123L;
    private final String URI_PAGAMENTO_ONLINE = "/api/v1/pagamento/online/" + PEDIDO_ID;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private TransacaoRepository transacaoRepository;
    @Autowired
    private ObjectMapper mapper;

    private Usuario usuario;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        transacaoRepository.deleteAll();
        usuarioRepository.deleteAll();
        restauranteRepository.deleteAll();
        usuario = usuarioRepository.save(new Usuario("email@a.com", CARTAO_CREDITO));
        restaurante = restauranteRepository.save(new Restaurante("Restaurante", CARTAO_CREDITO));
    }

    @AfterEach
    void tearDown() {
        transacaoRepository.deleteAll();
        usuarioRepository.deleteAll();
        restauranteRepository.deleteAll();
    }

    @Nested
    class Testes {

        @Test
        @DisplayName("Deve criar uma transação para um pagamento ONLINE, retornar 200")
        void teste1() throws Exception {
            var request = new NovoPagamentoOnlineRequest(usuario.getId(), restaurante.getId(), CARTAO_CREDITO);

            mockMvc.perform(post(URI_PAGAMENTO_ONLINE)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isOk());

            await().until(() -> transacaoRepository.findByPedidoId(PEDIDO_ID).getStatus() == CONCLUIDA);
            var transacoes = transacaoRepository.findAll();

            assertAll(
                    () -> assertEquals(1, transacoes.size()),
                    () -> assertEquals(CONCLUIDA, transacoes.get(0).getStatus())
            );
        }

        @Test
        @DisplayName("Não deve criar uma transação para um pagamento OFFLINE quando usuário não existir, retornar 404")
        void teste2() throws Exception {
            var request = new NovoPagamentoOnlineRequest(Long.MAX_VALUE, restaurante.getId(), CARTAO_CREDITO);

            var response = mockMvc.perform(post(URI_PAGAMENTO_ONLINE)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString(UTF_8);

            var transacoes = transacaoRepository.findAll();

            var resposta = (ExceptionHandlerResponse) fromJson(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertTrue(transacoes.isEmpty()),
                    () -> assertTrue(resposta.erros().get("mensagem").contains("Usuário não encontrado")),
                    () -> assertEquals(1, resposta.erros().get("mensagem").size())
            );
        }

        @Test
        @DisplayName("Não deve criar uma transação para um pagamento OFFLINE quando restaurante não existir, retornar 404")
        void teste3() throws Exception {
            var request = new NovoPagamentoOnlineRequest(usuario.getId(), Long.MAX_VALUE, CARTAO_CREDITO);

            var response = mockMvc.perform(post(URI_PAGAMENTO_ONLINE)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString(UTF_8);

            var transacoes = transacaoRepository.findAll();

            var resposta = (ExceptionHandlerResponse) fromJson(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertTrue(transacoes.isEmpty()),
                    () -> assertTrue(resposta.erros().get("mensagem").contains("Restaurante não encontrado")),
                    () -> assertEquals(1, resposta.erros().get("mensagem").size())
            );
        }

        @Test
        @DisplayName("Não deve criar uma transação para um pagamento OFFLINE por esse endpoint, retornar 400")
        void teste4() throws Exception {
            var request = new NovoPagamentoOnlineRequest(usuario.getId(), restaurante.getId(), DINHEIRO);

            var response = mockMvc.perform(post(URI_PAGAMENTO_ONLINE)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString(UTF_8);

            var transacoes = transacaoRepository.findAll();

            var resposta = (ExceptionHandlerResponse) fromJson(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertTrue(transacoes.isEmpty()),
                    () -> assertTrue(resposta.erros().get("mensagem").contains("Esse endpoint suporta apenas pagamentos Online")),
                    () -> assertEquals(1, resposta.erros().get("mensagem").size())
            );
        }

    }


    private String toJson(Object obj) throws JsonProcessingException {
        return this.mapper.writeValueAsString(obj);
    }

    public Object fromJson(String json, Class<?> classe) throws JsonProcessingException {
        return mapper.readValue(json, classe);
    }
}