package br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Reserva;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.StatusReservaEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReservaRepositoryTest {

    @Mock
    private ReservaRepository reservaRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveEncontrarReservasPorHorarioId() {
        // Mockando Dados
        Long idHorario = 1L;
        Reserva reserva1 = Reserva.builder()
                .idReserva(1L)
                .status(StatusReservaEnum.ATIVA)
                .horario(Horario.builder().idHorario(idHorario).build())
                .build();
        Reserva reserva2 = Reserva.builder()
                .idReserva(2L)
                .status(StatusReservaEnum.RESERVADO)
                .horario(Horario.builder().idHorario(idHorario).build())
                .build();

        List<Reserva> reservasMockadas = List.of(reserva1, reserva2);

        when(reservaRepository.findReservasByHorario_IdHorario(idHorario))
                .thenReturn(reservasMockadas);

        // Realizando Ação
        List<Reserva> reservas = reservaRepository.findReservasByHorario_IdHorario(idHorario);

        // Verificações
        assertThat(reservas).isNotEmpty().hasSize(2);
        assertThat(reservas).extracting(Reserva::getStatus)
                .containsExactly(StatusReservaEnum.ATIVA, StatusReservaEnum.RESERVADO);

        verify(reservaRepository, times(1)).findReservasByHorario_IdHorario(idHorario);
    }

    @Test
    void deveEncontrarReservasPorHorarioIdEStatus() {
        // Mockando Dados
        Long idHorario = 1L;
        String status = StatusReservaEnum.ATIVA.name();
        Reserva reserva = Reserva.builder()
                .idReserva(1L)
                .status(StatusReservaEnum.ATIVA)
                .horario(Horario.builder().idHorario(idHorario).build())
                .build();

        List<Reserva> reservasMockadas = List.of(reserva);

        when(reservaRepository.findReservasByHorario_IdHorarioAndStatus(idHorario, status))
                .thenReturn(reservasMockadas);

        // Ação
        List<Reserva> reservas = reservaRepository.findReservasByHorario_IdHorarioAndStatus(idHorario, status);

        // Verificações
        assertThat(reservas).isNotEmpty().hasSize(1);
        assertThat(reservas.get(0).getStatus()).isEqualTo(StatusReservaEnum.ATIVA);

        verify(reservaRepository, times(1))
                .findReservasByHorario_IdHorarioAndStatus(idHorario, status);
    }

    @Test
    void deveRetornarListaVaziaSeNenhumaReservaEncontrada() {
        // Mock
        when(reservaRepository.findReservasByHorario_IdHorario(999L))
                .thenReturn(Collections.emptyList());

        // Ação
        List<Reserva> reservas = reservaRepository.findReservasByHorario_IdHorario(999L);

        // Verificações
        assertThat(reservas).isEmpty();

        verify(reservaRepository, times(1)).findReservasByHorario_IdHorario(999L);
    }

}
