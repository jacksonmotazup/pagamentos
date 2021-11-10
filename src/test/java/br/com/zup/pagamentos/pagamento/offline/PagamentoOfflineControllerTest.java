package br.com.zup.pagamentos.pagamento.offline;

import br.com.zup.pagamentos.compartilhado.handler.ExceptionHandlerResponse;
import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.restaurante.RestauranteRepository;
import br.com.zup.pagamentos.transacao.Transacao;
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

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static br.com.zup.pagamentos.formapagamento.FormaPagamento.*;
import static br.com.zup.pagamentos.transacao.StatusTransacao.AGUARDANDO_CONFIRMACAO;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PagamentoOfflineControllerTest {

    private final Long ID_PEDIDO = 123L;
    private final String URI_PAGAMENTO_OFFLINE = "/api/v1/pagamento/offline/" + ID_PEDIDO;
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
        usuario = usuarioRepository.save(new Usuario("email@a.com", CARTAO_CREDITO, DINHEIRO, MAQUINA));
        restaurante = restauranteRepository.save(new Restaurante("Restaurante", CARTAO_CREDITO, CHEQUE, DINHEIRO));
    }

    @AfterEach
    void tearDown() {
        transacaoRepository.deleteAll();
        usuarioRepository.deleteAll();
        restauranteRepository.deleteAll();
    }

    @Nested
    class testes {

        @Test
        @DisplayName("Deve criar uma transação para um pagamento OFFLINE, retornar 200 e o Id da transação")
        void teste1() throws Exception {
            var request = new NovoPagamentoOfflineRequest(DINHEIRO, restaurante.getId(), usuario.getId());

            var response = mockMvc.perform(post(URI_PAGAMENTO_OFFLINE)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            var transacoes = transacaoRepository.findAll();

            assertAll(
                    () -> assertEquals(1, transacoes.size()),
                    () -> assertEquals(transacoes.get(0).getId(), Long.valueOf(response))
            );
        }

        @Test
        @DisplayName("Não deve criar uma transação para um pagamento OFFLINE quando usuário não existir, retornar 404")
        void teste2() throws Exception {
            var request = new NovoPagamentoOfflineRequest(DINHEIRO, restaurante.getId(), Long.MAX_VALUE);

            var response = mockMvc.perform(post(URI_PAGAMENTO_OFFLINE)
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
            var request = new NovoPagamentoOfflineRequest(DINHEIRO, Long.MAX_VALUE, usuario.getId());

            var response = mockMvc.perform(post(URI_PAGAMENTO_OFFLINE)
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
        @DisplayName("Não deve criar uma transação para um pagamento ONLINE por esse endpoint, retornar 400")
        void teste4() throws Exception {
            var request = new NovoPagamentoOfflineRequest(CARTAO_CREDITO, restaurante.getId(), usuario.getId());

            var response = mockMvc.perform(post(URI_PAGAMENTO_OFFLINE)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString(UTF_8);

            var transacoes = transacaoRepository.findAll();

            var resposta = (ExceptionHandlerResponse) fromJson(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertTrue(transacoes.isEmpty()),
                    () -> assertTrue(resposta.erros().get("mensagem").contains("Esse endpoint suporta apenas pagamentos Offline")),
                    () -> assertEquals(1, resposta.erros().get("mensagem").size())
            );
        }

        @Test
        @DisplayName("Não deve criar uma transação para um pagamento OFFLINE quando transação existir para esse pedido, " +
                "retornar 400")
        void teste5() throws Exception {
            var request = new NovoPagamentoOfflineRequest(DINHEIRO, restaurante.getId(), usuario.getId());

            var transacao = new Transacao(ID_PEDIDO, usuario, restaurante, BigDecimal.valueOf(10), DINHEIRO,
                    null, AGUARDANDO_CONFIRMACAO);
            transacaoRepository.save(transacao);

            var response = mockMvc.perform(post(URI_PAGAMENTO_OFFLINE)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString(UTF_8);

            var transacoes = transacaoRepository.findAll();

            var resposta = (ExceptionHandlerResponse) fromJson(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertEquals(1, transacoes.size()),
                    () -> assertTrue(resposta.erros().get("mensagem").contains("Transação já existe")),
                    () -> assertEquals(1, resposta.erros().get("mensagem").size())
            );
        }

        @Test
        @DisplayName("Não deve criar uma transação para um pagamento OFFLINE quando forma de pagamento não for aceita " +
                "pelo restaurante para esse pedido, retornar 400")
        void teste6() throws Exception {
            var request = new NovoPagamentoOfflineRequest(MAQUINA, restaurante.getId(), usuario.getId());

            var response = mockMvc.perform(post(URI_PAGAMENTO_OFFLINE)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString(UTF_8);

            var transacoes = transacaoRepository.findAll();

            var resposta = (ExceptionHandlerResponse) fromJson(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertEquals(0, transacoes.size()),
                    () -> assertTrue(resposta.erros().get("mensagem").contains("A combinação dessa forma de pagamento " +
                            "entre usuário e restaurante não é válida")),
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