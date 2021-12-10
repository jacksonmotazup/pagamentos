package br.com.zup.pagamentos.transacao;

import br.com.zup.pagamentos.compartilhado.handler.ExceptionHandlerResponse;
import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.restaurante.RestauranteRepository;
import br.com.zup.pagamentos.usuario.Usuario;
import br.com.zup.pagamentos.usuario.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static br.com.zup.pagamentos.formapagamento.FormaPagamento.*;
import static br.com.zup.pagamentos.transacao.StatusTransacao.CONCLUIDA;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransacaoControllerTest {

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
    private Transacao transacao;
    private String URI_TRANSACAO;

    @BeforeEach
    void setUp() {
        transacaoRepository.deleteAll();
        usuarioRepository.deleteAll();
        restauranteRepository.deleteAll();
        usuario = usuarioRepository.save(new Usuario("email@a.com", CARTAO_CREDITO, DINHEIRO, MAQUINA));
        restaurante = restauranteRepository.save(new Restaurante("Restaurante", CARTAO_CREDITO, CHEQUE, DINHEIRO));
        transacao = transacaoRepository.save(new Transacao(123L, usuario, restaurante, BigDecimal.valueOf(100),
                CARTAO_CREDITO, null, CONCLUIDA));

        URI_TRANSACAO = "/api/v1/transacoes?transacaoId=" + transacao.getId() + "&usuarioId=" + usuario.getId();
    }

    @AfterEach
    void tearDown() {
        transacaoRepository.deleteAll();
        usuarioRepository.deleteAll();
        restauranteRepository.deleteAll();
    }

    @Nested
    class DetalhesTransacao {

        @Test
        @DisplayName("Deve retornar os detalhes de uma transação existente, status http 200")
        void teste1() throws Exception {

            var response = mockMvc.perform(get(URI_TRANSACAO))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            var resposta = mapper.readValue(response, DetalhesTransacaoResponse.class);

            assertAll(
                    () -> assertEquals(transacao.getId(), resposta.getId()),
                    () -> assertEquals(transacao.getPedidoId(), resposta.getPedidoId()),
                    () -> assertEquals(transacao.getUsuario().getEmail(), resposta.getEmailUsuario()),
                    () -> assertEquals(transacao.getRestaurante().getNome(), resposta.getNomeRestaurante()),
                    () -> assertEquals(transacao.getValor(), resposta.getValor()),
                    () -> assertNotNull(resposta.getDataCriacao()),
                    () -> assertEquals(transacao.getFormaPagamento(), resposta.getFormaPagamento()),
                    () -> assertEquals(transacao.getInformacoes(), resposta.getInformacoes()),
                    () -> assertEquals(transacao.getStatus(), resposta.getStatus())
            );
        }

        @Test
        @DisplayName("Não deve retornar os detalhes de uma transação inexistente")
        void teste2() throws Exception {
            transacaoRepository.deleteAll();
            var response = mockMvc.perform(get(URI_TRANSACAO))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString(UTF_8);

            var resposta = mapper.readValue(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertEquals(1, resposta.erros().size()),
                    () -> assertTrue(resposta.erros().get("mensagem").contains("Transação não encontrada"))
            );
        }

        @Test
        @DisplayName("Não deve retornar os detalhes de uma transação quando usuário não existir")
        void teste3() throws Exception {
            var usuarioInexistente = usuarioRepository.count() + 1;
            var URI = "/api/v1/transacoes?transacaoId=" + transacao.getId() + "&usuarioId=" + usuarioInexistente;

            var response = mockMvc.perform(get(URI))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString(UTF_8);

            var resposta = mapper.readValue(response, ExceptionHandlerResponse.class);

            assertAll(
                    () -> assertEquals(1, resposta.erros().size()),
                    () -> assertTrue(resposta.erros().get("mensagem").contains("Transação não encontrada"))
            );
        }
    }

}