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
                    .andExpect(jsonPath("$.statusReserva").value(StatusReservaEnum.RESERVADO.name()));

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
                    .andExpect(jsonPath("$.statusReserva").value("RESERVADO"));

            verify(reservaService, times(1)).buscarPeloId(1L);
        }

        @Test
        void deveRetornarErroAoBuscarReservaInexistente() throws Exception {
            when(reservaService.buscarPeloId(1L)).thenThrow(new RegistroNotFoundException("Reserva", 1L));

            mockMvc.perform(get("/reserva/verificarReserva/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Reserva não encontrado com ID: 1"));

            verify(reservaService, times(1)).buscarPeloId(1L);
        }

        @Test
        void deveBuscarReservasParaHorarioPeloStatus() throws Exception {
            // Arrange
            Long idHorario = 1L;
            StatusReservaEnum status = StatusReservaEnum.RESERVADO;

            List<Reserva> reservasMock = List.of(
                    new Reserva(1L, status, new Usuario(), new Horario()),
                    new Reserva(2L, status, new Usuario(), new Horario())
            );

            when(reservaService.buscarTodasReservasDoHorarioComStatus(idHorario, status))
                    .thenReturn(reservasMock);

            // Act & Assert
            mockMvc.perform(get("/reserva/buscarReservasDoHorarioPeloStatus/1", idHorario)
                            .param("status", status.name()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(reservasMock.size()))
                    .andExpect(jsonPath("$[0].idReserva").value(reservasMock.get(0).getIdReserva()))
                    .andExpect(jsonPath("$[0].status").value(reservasMock.get(0).getStatus().name()))
                    .andExpect(jsonPath("$[1].idReserva").value(reservasMock.get(1).getIdReserva()))
                    .andExpect(jsonPath("$[1].status").value(reservasMock.get(1).getStatus().name()));

            // Verify interactions
            verify(reservaService, times(1)).buscarTodasReservasDoHorarioComStatus(idHorario, status);
        }

        @Test
        void deveRetornarErroAoBuscarReservasParaHorarioInexistenteComStatus() throws Exception {
            when(reservaService.buscarTodasReservasDoHorarioComStatus(1L, StatusReservaEnum.RESERVADO))
                    .thenThrow(new RegistroNotFoundException("Horario", 1L));

            mockMvc.perform(get("/reserva/buscarReservasDoHorarioPeloStatus/1")
                            .param("status", StatusReservaEnum.RESERVADO.name()))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Horario não encontrado com ID: 1"));

            verify(reservaService, times(1)).buscarTodasReservasDoHorarioComStatus(1L, StatusReservaEnum.RESERVADO);
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
                    .andExpect(jsonPath("$.statusReserva").value("CANCELADA"));

            verify(reservaService, times(1)).cancelarPeloId(1L);
        }

        @Test
        void deveRetornarErroAoCancelarReservaInvalida() throws Exception {
            when(reservaService.cancelarPeloId(1L)).thenThrow(new RegraDeNegocioException("Apenas pode se cancelar reservas que estejam RESERVADAS!"));

            mockMvc.perform(delete("/reserva/cancelarReserva/1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Apenas pode se cancelar reservas que estejam RESERVADAS!"));

            verify(reservaService, times(1)).cancelarPeloId(1L);
        }
    }

    @Nested
    class FinalizarReserva {

        @Test
        void deveFinalizarReservaComSucesso() throws Exception {
            Reserva reserva = new Reserva(1L, StatusReservaEnum.FINALIZADA, new Usuario(), new Horario());

            when(reservaService.finalizarPeloId(1L)).thenReturn(reserva);

            mockMvc.perform(put("/reserva/finalizarAtendimendoReserva/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idReserva").value(1L))
                    .andExpect(jsonPath("$.statusReserva").value("FINALIZADA"));

            verify(reservaService, times(1)).finalizarPeloId(1L);
        }

        @Test
        void deveRetornarErroAoFinalizarReservaInvalida() throws Exception {
            when(reservaService.finalizarPeloId(1L)).thenThrow(new RegraDeNegocioException("Apenas podem finalizar reservas que estejam ATIVAS!"));

            mockMvc.perform(put("/reserva/finalizarAtendimendoReserva/1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Apenas podem finalizar reservas que estejam ATIVAS!"));

            verify(reservaService, times(1)).finalizarPeloId(1L);
        }
    }

    @Nested
    class iniciarAtendimento {

        @Test
        void deveIniciarReservaComSucesso() throws Exception {
            Reserva reserva = new Reserva(1L, StatusReservaEnum.ATIVA, new Usuario(), new Horario());

            when(reservaService.iniciarPeloId(1L)).thenReturn(reserva);

            mockMvc.perform(put("/reserva/iniciarAtendimendoReserva/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idReserva").value(1L))
                    .andExpect(jsonPath("$.statusReserva").value("ATIVA"));

            verify(reservaService, times(1)).iniciarPeloId(1L);
        }

        @Test
        void deveRetornarErroAoIniciarReservaInvalida() throws Exception {
            when(reservaService.iniciarPeloId(1L)).thenThrow(new RegraDeNegocioException("Apenas podem iniciar reservas que estejam RESERVADAS!"));

            mockMvc.perform(put("/reserva/iniciarAtendimendoReserva/1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Apenas podem iniciar reservas que estejam RESERVADAS!"));

            verify(reservaService, times(1)).iniciarPeloId(1L);
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
