package br.com.techchallenge.ratatouille.adapter.controller;

import br.com.techchallenge.ratatouille.ratatouille.adapter.controller.RestauranteController;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.HorarioDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.RestauranteDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.HorarioMapper;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.LocalizacaoMapper;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.RestauranteMapper;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Horario;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

            mockMvc.perform(post("/restaurante/cadastrarRestaurante")
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

            mockMvc.perform(post("/restaurante/cadastrarRestaurante")
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

            mockMvc.perform(get("/restaurante/buscarRestaurante/1"))
                    .andExpect(status().isFound())
                    .andExpect(jsonPath("$.nome").value("Restaurante A"));

            verify(restauranteService, times(1)).buscarPeloId(eq(1L));
        }

        @Test
        void deveRetornarErroSeNaoEncontrado() throws Exception {
            when(restauranteService.buscarPeloId(eq(1L))).thenThrow(new RegistroNotFoundException("Restaurante", 1L));

            mockMvc.perform(get("/restaurante/buscarRestaurante/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Restaurante não encontrado com ID: 1"));
        }

        @Test
        void deveRetornarRestaurantesPeloNome() throws Exception {
            Restaurante restaurante = new Restaurante(1L, "Restaurante A", new Localizacao(), TipoDeCozinhaEnum.ITALIANA);
            List<Restaurante> restaurantes = List.of(restaurante);

            when(restauranteService.buscarPeloNome(eq("Restaurante A"))).thenReturn(restaurantes);

            mockMvc.perform(get("/restaurante/buscarRestaurantePorNome/Restaurante A"))
                    .andExpect(status().isFound())
                    .andExpect(jsonPath("$[0].nome").value("Restaurante A"));

            verify(restauranteService, times(1)).buscarPeloNome(eq("Restaurante A"));
        }

        @Test
        void deveRetornarErroSeNenhumRestauranteEncontrado() throws Exception {
            when(restauranteService.buscarPeloNome(eq("Restaurante A"))).thenThrow(new RegistroNotFoundException("Restaurante", 0L));

            mockMvc.perform(get("/restaurante/buscarRestaurantePorNome/Restaurante A"))
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

        @Test
        void deveAdicionarHorarioComSucesso() throws Exception {

            fail("Não foi possível implementar devido aos problemas com a biblioteca de time do java 8.");
            Horario horario = new Horario();
            horario.setIdHorario(1L);
            horario.setHoraInicio(LocalTime.of(9, 0));
            horario.setHoraFim(LocalTime.of(17, 0));
            horario.setData(LocalDate.now());
            horario.setEspacosParaReserva(10);
            horario.setQtdReservados(0);
            horario.setRestaurante(new Restaurante());

            when(horarioService.adicionarHorarioDeFuncionamentoAoRestaurante(1L, horario))
                    .thenReturn(horario);

            mockMvc.perform(post("/restaurante/adicionarHorario/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(horario)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.horaInicio[0]").value(9))
                    .andExpect(jsonPath("$.horaInicio[1]").value(0));
            verify(horarioService).adicionarHorarioDeFuncionamentoAoRestaurante(1L, horario);
        }

        @Test
        void deveRetornarErro_IdJaExistente() throws Exception {
            fail("Não foi possível implementar devido aos problemas com a biblioteca de time do java 8.");

            Horario horario = new Horario();
            horario.setIdHorario(1L);
            horario.setHoraInicio(LocalTime.of(9, 0));
            horario.setHoraFim(LocalTime.of(17, 0));
            horario.setData(LocalDate.now());
            horario.setEspacosParaReserva(10);
            horario.setQtdReservados(0);
            horario.setRestaurante(new Restaurante());

            HorarioDTO horarioDTO;
            horarioDTO = HorarioMapper.toDTO(horario);

            when(horarioService.adicionarHorarioDeFuncionamentoAoRestaurante(1L, horario))
                    .thenThrow(new IdJaExistenteException("Id do Horario já existente!"));

            mockMvc.perform(post("/restaurante/adicionarHorario/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(horarioDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Id do Horario já existente!"));
        }

        @Test
        void deveBuscarHorarioPorIdComSucesso() throws Exception {
            Horario horario = new Horario();
            horario.setIdHorario(1L);
            horario.setHoraInicio(LocalTime.of(9, 0));
            horario.setHoraFim(LocalTime.of(17, 0));
            horario.setData(LocalDate.now());
            horario.setEspacosParaReserva(10);
            horario.setQtdReservados(0);
            horario.setRestaurante(new Restaurante());

            when(horarioService.buscarPeloId(1L)).thenReturn(horario);

            mockMvc.perform(get("/restaurante/buscarHorarioPeloId/1"))
                    .andExpect(status().isFound())
                    .andExpect(jsonPath("$.horaInicio[0]").value(9))
                    .andExpect(jsonPath("$.horaInicio[1]").value(0));

            verify(horarioService).buscarPeloId(1L);
        }

        @Test
        void deveRetornarNotFound_QuandoHorarioNaoExistir() throws Exception {
            when(horarioService.buscarPeloId(1L))
                    .thenThrow(new RegistroNotFoundException("Horario", 1L));

            mockMvc.perform(get("/restaurante/buscarHorarioPeloId/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Horario não encontrado com ID: 1"));
        }

        @Test
        void deveAtualizarHorarioComSucesso() throws Exception {
            fail("Não foi possível implementar devido aos problemas com a biblioteca de time do java 8.");

            Horario horario = new Horario();
            horario.setIdHorario(1L);
            horario.setHoraInicio(LocalTime.of(9, 0));
            horario.setHoraFim(LocalTime.of(17, 0));
            horario.setData(LocalDate.now());
            horario.setEspacosParaReserva(10);
            horario.setQtdReservados(0);
            horario.setRestaurante(new Restaurante());

            HorarioDTO horarioDTO;
            horarioDTO = HorarioMapper.toDTO(horario);


            when(horarioService.atualizarHorario(eq(1L), any())).thenReturn(horario);

            mockMvc.perform(put("/restaurante/atualizarHorario/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(horarioDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.horaInicio[0]").value(9))
                    .andExpect(jsonPath("$.horaInicio[1]").value(0));
        }

        @Test
        void deveDeletarHorarioComSucesso() throws Exception {
            doNothing().when(horarioService).deletarPeloId(1L);

            mockMvc.perform(delete("/restaurante/deletarHorario/1"))
                    .andExpect(status().isOk());

            verify(horarioService).deletarPeloId(1L);
        }
        @Test
        void deveAtualizarCapacidadeComSucesso() throws Exception {

            Horario horario = new Horario();
            horario.setIdHorario(1L);
            horario.setHoraInicio(LocalTime.of(9, 0));
            horario.setHoraFim(LocalTime.of(17, 0));
            horario.setData(LocalDate.now());
            horario.setEspacosParaReserva(10);
            horario.setQtdReservados(0);
            horario.setRestaurante(new Restaurante());

            horario.setEspacosParaReserva(20);
            when(horarioService.atualizarQuantidadeDeReservasMaximas(1L, 20))
                    .thenReturn(horario);

            mockMvc.perform(put("/restaurante/atualizarCapacidadeDeReservasNoHorario/1")
                            .param("capacidade", "20"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.espacosParaReserva").value(20));
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
