package br.com.techchallenge.ratatouille.adapter.controller;

import br.com.techchallenge.ratatouille.ratatouille.adapter.controller.ReservaController;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.ReservaDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegraDeNegocioException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.ReservaMapper;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.*;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.StatusReservaEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ReservaControllerTest {

    @Mock
    private ReservaService reservaService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReservaController reservaController = new ReservaController(reservaService);
        mockMvc = MockMvcBuilders.standaloneSetup(reservaController).build();
    }

    @Nested
    class RealizarReserva {

        @Test
        void deveCriarReservaComSucesso() throws Exception {
            ReservaDTO reservaDTO = new ReservaDTO(null,StatusReservaEnum.RESERVADO, new Usuario(), new Horario());
            Reserva reserva = ReservaMapper.toEntity(reservaDTO);
            reserva.setIdReserva(1L);

            when(reservaService.adicionarReservaParaHorario(eq(1L), any(Reserva.class))).thenReturn(reserva);

            mockMvc.perform(post("/reserva/realizarReserva/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(reservaDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idReserva").value(1L))
                    .andExpect(jsonPath("$.status").value("RESERVADO"));

            verify(reservaService, times(1)).adicionarReservaParaHorario(eq(1L), any(Reserva.class));
        }

        @Test
        void deveRetornarErroAoCriarReservaComDadosInvalidos() throws Exception {
            ReservaDTO reservaDTO = new ReservaDTO(null,StatusReservaEnum.RESERVADO, new Usuario(), new Horario());

            when(reservaService.adicionarReservaParaHorario(eq(1L), any(Reserva.class)))
                    .thenThrow(new RegraDeNegocioException("Erro de negócio!"));

            mockMvc.perform(post("/reserva/realizarReserva/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(reservaDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Erro de negócio!"));

            verify(reservaService, times(1)).adicionarReservaParaHorario(eq(1L), any(Reserva.class));
        }
    }

    @Nested
    class BuscarReserva {

        @Test
        void deveBuscarReservaPorIdComSucesso() throws Exception {
            Reserva reserva = new Reserva(1L, StatusReservaEnum.RESERVADO, new Usuario(), new Horario());

            when(reservaService.buscarPeloId(1L)).thenReturn(reserva);

            mockMvc.perform(get("/reserva/verificarReserva/1"))
                    .andExpect(status().isFound())
                    .andExpect(jsonPath("$.idReserva").value(1L))
                    .andExpect(jsonPath("$.status").value("RESERVADO"));

            verify(reservaService, times(1)).buscarPeloId(1L);
        }

        @Test
        void deveRetornarErroAoBuscarReservaInexistente() throws Exception {
            when(reservaService.buscarPeloId(1L)).thenThrow(new RegistroNotFoundException("Reserva", 1L));

            mockMvc.perform(get("/reserva/verificarReserva/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Reserva com id 1 não encontrada."));

            verify(reservaService, times(1)).buscarPeloId(1L);
        }
    }

    @Nested
    class CancelarReserva {

        @Test
        void deveCancelarReservaComSucesso() throws Exception {
            Reserva reserva = new Reserva(1L, StatusReservaEnum.CANCELADA, new Usuario(), new Horario());

            when(reservaService.cancelarPeloId(1L)).thenReturn(reserva);

            mockMvc.perform(delete("/reserva/cancelarReserva/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idReserva").value(1L))
                    .andExpect(jsonPath("$.status").value("CANCELADA"));

            verify(reservaService, times(1)).cancelarPeloId(1L);
        }

        @Test
        void deveRetornarErroAoCancelarReservaInvalida() throws Exception {
            when(reservaService.cancelarPeloId(1L)).thenThrow(new RegraDeNegocioException("Erro ao cancelar reserva!"));

            mockMvc.perform(delete("/reserva/cancelarReserva/1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Erro ao cancelar reserva!"));

            verify(reservaService, times(1)).cancelarPeloId(1L);
        }
    }

    @Nested
    class BuscarReservasDoHorario {

        @Test
        void deveBuscarReservasDoHorarioComSucesso() throws Exception {
            List<Reserva> reservas = List.of(
                    new Reserva(1L, StatusReservaEnum.RESERVADO, new Usuario(), new Horario()),
                    new Reserva(2L, StatusReservaEnum.ATIVA, new Usuario(), new Horario())
            );

            when(reservaService.buscarTodasReservasDoHorario(1L)).thenReturn(reservas);

            mockMvc.perform(get("/reserva/buscarReservasDoHorario/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].idReserva").value(1L))
                    .andExpect(jsonPath("$[0].status").value("RESERVADO"))
                    .andExpect(jsonPath("$[1].idReserva").value(2L))
                    .andExpect(jsonPath("$[1].status").value("ATIVA"));

            verify(reservaService, times(1)).buscarTodasReservasDoHorario(1L);
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
