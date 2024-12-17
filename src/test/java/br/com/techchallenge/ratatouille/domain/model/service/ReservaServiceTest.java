package br.com.techchallenge.ratatouille.domain.model.service;


import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegraDeNegocioException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Reserva;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.StatusReservaEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.HorarioService;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.ReservaServiceImpl;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.RestauranteServiceImpl;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.UsuarioService;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.ReservaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservaServiceTest {

    private ReservaServiceImpl reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private HorarioService horarioService;

    @Mock
    private UsuarioService usuarioService;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
        this.reservaService = new ReservaServiceImpl( reservaRepository, horarioService, usuarioService);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }

    @Nested
    class AdicionarReservaParaHorario {

        @Test
        void deveAdicionarReservaQuandoHorarioDisponivel() {
            Long idHorario = 1L;
            Horario horario = new Horario();
            horario.setIdHorario(idHorario);
            horario.setQtdReservados(2);
            horario.setEspacosParaReserva(5);

            Reserva reserva = new Reserva();
            reserva.setIdReserva(1L);

            when(horarioService.buscarPeloId(idHorario)).thenReturn(horario);
            when(reservaRepository.existsById(reserva.getIdReserva())).thenReturn(false);
            when(reservaRepository.save(reserva)).thenReturn(reserva);

            Reserva resultado = reservaService.adicionarReservaParaHorario(idHorario, reserva);

            assertNotNull(resultado);
            assertEquals(reserva, resultado);
            verify(horarioService).incrementaQuantidadeReservas(idHorario);
        }

        @Test
        void deveLancarExcecaoQuandoHorarioCheio() {
            Long idHorario = 1L;
            Horario horario = new Horario();
            horario.setIdHorario(idHorario);
            horario.setQtdReservados(5);
            horario.setEspacosParaReserva(5);

            Reserva reserva = new Reserva();
            reserva.setIdReserva(1L);

            when(horarioService.buscarPeloId(idHorario)).thenReturn(horario);

            assertThrows(RegraDeNegocioException.class, () ->
                    reservaService.adicionarReservaParaHorario(idHorario, reserva));
        }
    }

    @Nested
    class BuscarPeloId {

        @Test
        void deveRetornarReservaQuandoEncontrada() {
            Long idReserva = 1L;
            Reserva reserva = new Reserva();
            reserva.setIdReserva(idReserva);

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));

            Reserva resultado = reservaService.buscarPeloId(idReserva);

            assertNotNull(resultado);
            assertEquals(reserva, resultado);
        }

        @Test
        void deveLancarExcecaoQuandoNaoEncontrada() {
            Long idReserva = 1L;

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.empty());

            assertThrows(RegistroNotFoundException.class, () -> reservaService.buscarPeloId(idReserva));
        }
    }

    @Nested
    class CancelarPeloId {

        @Test
        void deveCancelarReservaQuandoStatusEhReservado() {
            Long idReserva = 1L;
            Reserva reserva = new Reserva();
            reserva.setIdReserva(idReserva);
            reserva.setStatus(StatusReservaEnum.RESERVADO);

            Horario horario = new Horario();
            horario.setIdHorario(1L);
            reserva.setHorario(horario);

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));
            when(reservaRepository.save(reserva)).thenReturn(reserva);

            Reserva resultado = reservaService.cancelarPeloId(idReserva);

            assertNotNull(resultado);
            assertEquals(StatusReservaEnum.CANCELADA, resultado.getStatus());
            verify(horarioService).decrementaQuantidadeReservas(horario.getIdHorario());
        }

        @Test
        void deveLancarExcecaoQuandoStatusNaoEhReservado() {
            Long idReserva = 1L;
            Reserva reserva = new Reserva();
            reserva.setIdReserva(idReserva);
            reserva.setStatus(StatusReservaEnum.ATIVA);

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));

            assertThrows(RegraDeNegocioException.class, () -> reservaService.cancelarPeloId(idReserva));
        }
    }

    @Nested
    class FinalizarPeloId {

        @Test
        void deveFinalizarReservaQuandoStatusEhAtiva() {
            Long idReserva = 1L;
            Reserva reserva = new Reserva();
            reserva.setIdReserva(idReserva);
            reserva.setStatus(StatusReservaEnum.ATIVA);

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));
            when(reservaRepository.save(reserva)).thenReturn(reserva);

            Reserva resultado = reservaService.finalizarPeloId(idReserva);

            assertNotNull(resultado);
            assertEquals(StatusReservaEnum.FINALIZADA, resultado.getStatus());
        }

        @Test
        void deveLancarExcecaoQuandoStatusNaoEhAtiva() {
            Long idReserva = 1L;
            Reserva reserva = new Reserva();
            reserva.setIdReserva(idReserva);
            reserva.setStatus(StatusReservaEnum.CANCELADA);

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));

            assertThrows(RegraDeNegocioException.class, () -> reservaService.finalizarPeloId(idReserva));
        }
    }

    @Nested
    class IniciarPeloId {

        @Test
        void deveIniciarReservaQuandoStatusEhReservado() {
            Long idReserva = 1L;
            Reserva reserva = new Reserva();
            reserva.setIdReserva(idReserva);
            reserva.setStatus(StatusReservaEnum.RESERVADO);

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));
            when(reservaRepository.save(reserva)).thenReturn(reserva);

            Reserva resultado = reservaService.iniciarPeloId(idReserva);

            assertNotNull(resultado);
            assertEquals(StatusReservaEnum.ATIVA, resultado.getStatus());
        }

        @Test
        void deveLancarExcecaoQuandoStatusNaoEhReservado() {
            Long idReserva = 1L;
            Reserva reserva = new Reserva();
            reserva.setIdReserva(idReserva);
            reserva.setStatus(StatusReservaEnum.CANCELADA);

            when(reservaRepository.findById(idReserva)).thenReturn(Optional.of(reserva));

            assertThrows(RegraDeNegocioException.class, () -> reservaService.iniciarPeloId(idReserva));
        }
    }
}
