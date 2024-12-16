package br.com.techchallenge.ratatouille.adapter.controller;

import br.com.techchallenge.ratatouille.ratatouille.adapter.controller.RestauranteController;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.RestauranteDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.TipoDeCozinhaEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.HorarioService;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.LocalizacaoService;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.RestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class RestauranteControllerTest {

    @Mock
    private RestauranteService restauranteService;

    @Mock
    private LocalizacaoService localizacaoService;

    @Mock
    private HorarioService horarioService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        RestauranteController restauranteController = new RestauranteController(restauranteService,localizacaoService,horarioService);
        mockMvc = MockMvcBuilders.standaloneSetup(restauranteController).build();
    }

    @Nested
    class CriarRestaurante {

        @Test
        void deveCriarRestauranteComSucesso() throws Exception {
            RestauranteDTO restauranteDTO = new RestauranteDTO("Restaurante A", TipoDeCozinhaEnum.ITALIANA);
            Restaurante restaurante = new Restaurante(1L, "Restaurante A", new Localizacao(), TipoDeCozinhaEnum.ITALIANA);

            when(restauranteService.criar(any(Restaurante.class))).thenReturn(restaurante);

            mockMvc.perform(MockMvcRequestBuilders.post("/restaurante/cadastrarRestaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(restauranteDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nome").value("Restaurante A"));

            verify(restauranteService, times(1)).criar(any(Restaurante.class));
        }

        @Test
        void deveRetornarErroIdDuplicado() throws Exception {
            RestauranteDTO restauranteDTO = new RestauranteDTO("Restaurante A", TipoDeCozinhaEnum.ITALIANA);

            when(restauranteService.criar(any(Restaurante.class))).thenThrow(new IdJaExistenteException("Id do restaurante já existente!"));

            mockMvc.perform(MockMvcRequestBuilders.post("/restaurante/cadastrarRestaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(restauranteDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Id do restaurante já existente!"));
        }
    }

    @Nested
    class BuscarRestaurantePorId {

        @Test
        void deveRetornarRestaurantePeloId() throws Exception {
            Restaurante restaurante = new Restaurante(1L, "Restaurante A", new Localizacao(), TipoDeCozinhaEnum.ITALIANA);

            when(restauranteService.buscarPeloId(eq(1L))).thenReturn(restaurante);

            mockMvc.perform(MockMvcRequestBuilders.get("/restaurante/buscarRestaurante/1"))
                    .andExpect(status().isFound())
                    .andExpect(jsonPath("$.nome").value("Restaurante A"));

            verify(restauranteService, times(1)).buscarPeloId(eq(1L));
        }

        @Test
        void deveRetornarErroSeNaoEncontrado() throws Exception {
            when(restauranteService.buscarPeloId(eq(1L))).thenThrow(new RegistroNotFoundException("Restaurante", 1L));

            mockMvc.perform(MockMvcRequestBuilders.get("/restaurante/buscarRestaurante/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Registro 'Restaurante' com id '1' não encontrado!"));
        }
    }

    @Nested
    class BuscarRestaurantePorNome {

        @Test
        void deveRetornarRestaurantesPeloNome() throws Exception {
            Restaurante restaurante = new Restaurante(1L, "Restaurante A", new Localizacao(), TipoDeCozinhaEnum.ITALIANA);
            List<Restaurante> restaurantes = List.of(restaurante);

            when(restauranteService.buscarPeloNome(eq("Restaurante A"))).thenReturn(restaurantes);

            mockMvc.perform(MockMvcRequestBuilders.get("/restaurante/buscarRestaurantePorNome/Restaurante A"))
                    .andExpect(status().isFound())
                    .andExpect(jsonPath("$[0].nome").value("Restaurante A"));

            verify(restauranteService, times(1)).buscarPeloNome(eq("Restaurante A"));
        }

        @Test
        void deveRetornarErroSeNenhumRestauranteEncontrado() throws Exception {
            when(restauranteService.buscarPeloNome(eq("Restaurante A"))).thenThrow(new RegistroNotFoundException("Restaurante", 0L));

            mockMvc.perform(MockMvcRequestBuilders.get("/restaurante/buscarRestaurantePorNome/Restaurante A"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Registro 'Restaurante' com id '0' não encontrado!"));
        }
    }

    @Nested
    class RemoverRestaurante {

        @Test
        void deveRemoverRestauranteComSucesso() throws Exception {
            doNothing().when(restauranteService).remover(eq(1L));

            mockMvc.perform(MockMvcRequestBuilders.delete("/restaurante/removerRestaurante/1"))
                    .andExpect(status().isNoContent());

            verify(restauranteService, times(1)).remover(eq(1L));
        }

        @Test
        void deveRetornarErroSeNaoEncontradoParaRemocao() throws Exception {
            doThrow(new RegistroNotFoundException("Restaurante", 1L)).when(restauranteService).remover(eq(1L));

            mockMvc.perform(MockMvcRequestBuilders.delete("/restaurante/removerRestaurante/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Registro 'Restaurante' com id '1' não encontrado!"));
        }
    }

    @Nested
    class AtualizarRestaurante {

        @Test
        void deveAtualizarRestauranteComSucesso() throws Exception {
            RestauranteDTO restauranteDTO = new RestauranteDTO("Restaurante B", TipoDeCozinhaEnum.JAPONESA);
            Restaurante restauranteAtualizado = new Restaurante(1L, "Restaurante B", new Localizacao(), TipoDeCozinhaEnum.JAPONESA);

            when(restauranteService.atualizar(eq(1L), any(RestauranteDTO.class))).thenReturn(restauranteAtualizado);

            mockMvc.perform(MockMvcRequestBuilders.put("/restaurante/atualizarRestaurante/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(restauranteDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("Restaurante B"));

            verify(restauranteService, times(1)).atualizar(eq(1L), any(RestauranteDTO.class));
        }

        @Test
        void deveRetornarErroSeNaoEncontradoParaAtualizacao() throws Exception {
            RestauranteDTO restauranteDTO = new RestauranteDTO("Restaurante B", TipoDeCozinhaEnum.JAPONESA);

            when(restauranteService.atualizar(eq(1L), any(RestauranteDTO.class))).thenThrow(new RegistroNotFoundException("Restaurante", 1L));

            mockMvc.perform(MockMvcRequestBuilders.put("/restaurante/atualizarRestaurante/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(restauranteDTO)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Registro 'Restaurante' com id '1' não encontrado!"));
        }
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
