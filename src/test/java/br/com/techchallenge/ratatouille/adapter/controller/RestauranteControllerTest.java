package br.com.techchallenge.ratatouille.adapter.controller;

import br.com.techchallenge.ratatouille.ratatouille.adapter.controller.RestauranteController;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.RestauranteDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.LocalizacaoMapper;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.RestauranteMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
            RestauranteDTO restauranteDTO = new RestauranteDTO(null,"Restaurante A",new Localizacao(), TipoDeCozinhaEnum.ITALIANA);
            Restaurante restaurante = new Restaurante(1L,"Restaurante A",new Localizacao(), TipoDeCozinhaEnum.ITALIANA);

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
            RestauranteDTO restauranteDTO = new RestauranteDTO(null,"Restaurante A",new Localizacao(), TipoDeCozinhaEnum.ITALIANA);

            when(restauranteService.criar(any(Restaurante.class))).thenThrow(new IdJaExistenteException("Id do restaurante já existente!"));

            mockMvc.perform(MockMvcRequestBuilders.post("/restaurante/cadastrarRestaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(restauranteDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Id do restaurante já existente!"));
        }
    }

    @Nested
    class BuscarRestaurante{

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
                    .andExpect(content().string("Restaurante não encontrado com ID: 1"));
        }

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
                    .andExpect(content().string("Restaurante não encontrado com ID: 0"));
        }
    }

    @Nested
    class AtualizarRestaurante {

        @Test
        void atualizarDadosRestauranteComSucesso() throws Exception {

            Localizacao localizacao = new Localizacao();
            localizacao.setIdLocalizacao(1L);
            localizacao.setEstado("Estado");
            localizacao.setCidade("Cidade");
            localizacao.setBairro("Bairro");
            localizacao.setRua("Rua");
            localizacao.setNumero("123");

            Restaurante restaurante = new Restaurante();
            restaurante.setIdRestaurante(1L);
            restaurante.setNome("Restaurante Teste");
            restaurante.setLocalizacao(localizacao);
            restaurante.setTipoDeCozinha(TipoDeCozinhaEnum.BRASILEIRA);

            RestauranteDTO restauranteDTO = RestauranteMapper.toDTO(restaurante);

            when(restauranteService.atualizarDados(1L, "Restaurante Teste", TipoDeCozinhaEnum.BRASILEIRA))
                    .thenReturn(restaurante);

            mockMvc.perform(put("/restaurante/atualizarDadosRestaurante/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(restauranteDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("Restaurante Teste"))
                    .andExpect(jsonPath("$.localizacao.cidade").value(restauranteDTO.localizacao().getCidade()))
                    .andExpect(jsonPath("$.tipoDeCozinhaEnum").value(restauranteDTO.tipoDeCozinhaEnum().name()));
        }

        @Test
        void deveAtualizarLocalizacaoComSucesso() throws Exception {

            Localizacao localizacao = new Localizacao();
            localizacao.setIdLocalizacao(1L);
            localizacao.setEstado("Estado");
            localizacao.setCidade("Cidade");
            localizacao.setBairro("Bairro");
            localizacao.setRua("Rua");
            localizacao.setNumero("123");

            Restaurante restaurante = new Restaurante();
            restaurante.setIdRestaurante(1L);
            restaurante.setNome("Restaurante Teste");
            restaurante.setLocalizacao(localizacao);
            restaurante.setTipoDeCozinha(TipoDeCozinhaEnum.BRASILEIRA);

            RestauranteDTO restauranteDTO = RestauranteMapper.toDTO(restaurante);

            when(restauranteService.atualizarLocalizacao(1L, localizacao)).
                    thenReturn(restaurante);

            mockMvc.perform(put("/restaurante/atualizarLocalizacaoRestaurante/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(LocalizacaoMapper.toDTO(localizacao))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("Restaurante Teste"))
                    .andExpect(jsonPath("$.localizacao.idLocalizacao").value(restauranteDTO.localizacao().getIdLocalizacao()))
                    .andExpect(jsonPath("$.localizacao.estado").value(restauranteDTO.localizacao().getEstado()))
                    .andExpect(jsonPath("$.localizacao.cidade").value(restauranteDTO.localizacao().getCidade()))
                    .andExpect(jsonPath("$.localizacao.bairro").value(restauranteDTO.localizacao().getBairro()))
                    .andExpect(jsonPath("$.localizacao.rua").value(restauranteDTO.localizacao().getRua()))
                    .andExpect(jsonPath("$.localizacao.numero").value(restauranteDTO.localizacao().getNumero()));
        }
    }

    @Nested
    class manipularHorarioEReservas{

    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
