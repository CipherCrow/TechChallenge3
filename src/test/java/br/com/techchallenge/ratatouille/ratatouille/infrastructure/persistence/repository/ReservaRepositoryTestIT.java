package br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Reserva;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.SexoUsuarioEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.StatusReservaEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.UsuarioStatusEnum;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class ReservaRepositoryTestIT {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveSalvarERecuperarReservasPorHorarioId() {
        // Configurando dados
        Horario horario = Horario.builder()
                .horaInicio(LocalTime.of(10, 0))
                .horaFim(LocalTime.of(12, 0))
                .data(LocalDate.now())
                .espacosParaReserva(10)
                .qtdReservados(0)
                .build();
        horario = horarioRepository.save(horario);

        Usuario usuario = Usuario.builder()
                .nome("Cliente Teste")
                .email("cliente@teste.com")
                .idade(22)
                .sexo(SexoUsuarioEnum.MASCULINO)
                .status(UsuarioStatusEnum.ATIVO)
                .build();
        usuario = usuarioRepository.save(usuario);

        Reserva reserva = Reserva.builder()
                .status(StatusReservaEnum.ATIVA)
                .horario(horario)
                .cliente(usuario)
                .build();
        reserva = reservaRepository.save(reserva);

        // Ação
        List<Reserva> reservas = reservaRepository.findReservasByHorario_IdHorario(horario.getIdHorario());

        // Verificações
        assertThat(reservas).isNotEmpty();
        assertThat(reservas).hasSize(1);
        assertThat(reservas.get(0).getStatus()).isEqualTo(StatusReservaEnum.ATIVA);
        assertThat(reservas.get(0).getCliente().getNome()).isEqualTo("Cliente Teste");
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
