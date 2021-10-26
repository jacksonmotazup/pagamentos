package br.com.zup.pagamentos.listapagamentos;

import br.com.zup.pagamentos.restaurante.Restaurante;
import br.com.zup.pagamentos.restaurante.RestauranteRepository;
import br.com.zup.pagamentos.usuario.Usuario;
import br.com.zup.pagamentos.usuario.UsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static br.com.zup.pagamentos.formapagamento.FormaPagamento.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ListaPagamentosControllerTest {

    public static final String URI_PAGAMENTOS = "/api/v1/pagamentos";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;

    private Usuario usuario;
    private Restaurante restaurante;
    private Restaurante restaurante2;

    @Autowired
    private ObjectMapper mapper;


    @BeforeEach
    void setUp() {
        usuario = usuarioRepository.save(new Usuario("email@a.com", CARTAO_CREDITO, DINHEIRO, MAQUINA));
        restaurante = restauranteRepository.save(new Restaurante("Restaurante", CARTAO_CREDITO, CHEQUE, DINHEIRO));
        restaurante2 = restauranteRepository.save(new Restaurante("Restaurante2", CHEQUE));
    }

    @Nested
    class Testes {

        @Test
        @DisplayName("Deve retornar lista de formas de pagamento aceitas por usuario e pelo restaurante, retornar 200")
        void deveRetornarListaFormasPagamentoEmComumUsuarioERestaurante() throws Exception {
            var request = new ListaPagamentosRequest(usuario.getId(), restaurante.getId());

            var response = mockMvc.perform(get(URI_PAGAMENTOS)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            var resposta = (PagamentosResponse) fromJson(response, PagamentosResponse.class);

            assertAll(
                    () -> assertEquals(2, resposta.formasPagamento().size()),
                    () -> assertTrue(resposta.formasPagamento().contains(CARTAO_CREDITO)),
                    () -> assertTrue(resposta.formasPagamento().contains(DINHEIRO)),
                    () -> assertFalse(resposta.formasPagamento().contains(CHEQUE)),
                    () -> assertFalse(resposta.formasPagamento().contains(MAQUINA))
            );
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houverem formas de pagamento em comum, retornar 200")
        void deveRetornarListaVaziaQuandoNaoHouverFormaEmComum() throws Exception {
            var request = new ListaPagamentosRequest(usuario.getId(), restaurante2.getId());

            var response = mockMvc.perform(get(URI_PAGAMENTOS)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            var resposta = (PagamentosResponse) fromJson(response, PagamentosResponse.class);

            assertAll(
                    () -> assertEquals(0, resposta.formasPagamento().size()),
                    () -> assertFalse(resposta.formasPagamento().contains(CARTAO_CREDITO)),
                    () -> assertFalse(resposta.formasPagamento().contains(DINHEIRO)),
                    () -> assertFalse(resposta.formasPagamento().contains(CHEQUE)),
                    () -> assertFalse(resposta.formasPagamento().contains(MAQUINA))
            );
        }

        @Test
        @DisplayName("Não deve retornar lista de formas de pagamento quando usuário não existir, retornar 404")
        void naoDeveRetornarListaDeFormasDePagamentoQuandoUsuarioNaoExistir() throws Exception {
            var request = new ListaPagamentosRequest(Long.MAX_VALUE, restaurante.getId());

            var response = mockMvc.perform(get(URI_PAGAMENTOS)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString();

            assertAll(
                    () -> assertEquals("Usuário não encontrado", response)
            );
        }

        @Test
        @DisplayName("Não deve retornar lista de formas de pagamento quando restaurante não existir, retornar 404")
        void naoDeveRetornarListaDeFormasDePagamentoQuandoRestauranteNaoExistir() throws Exception {
            var request = new ListaPagamentosRequest(usuario.getId(), Long.MAX_VALUE);

            var response = mockMvc.perform(get(URI_PAGAMENTOS)
                            .contentType(APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString();

            assertAll(
                    () -> assertEquals("Restaurante não encontrado", response)
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