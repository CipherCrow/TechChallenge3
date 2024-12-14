package domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.QuantidadeDeReservasException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.HorarioServiceImpl;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.HorarioRepository;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.RestauranteRepository;import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HorarioServiceTest {

    private HorarioServiceImpl horarioService;

    @Mock
    private HorarioRepository horarioRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
        this.horarioService = new HorarioServiceImpl(horarioRepository,restauranteRepository);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }

    @Nested
    class criarHorario{
        @Test
        void deveAdicionarHorarioDeFuncionamentoAoRestaurante_ComSucesso() {
            // Arrange
            Long restauranteId = 1L;
            Restaurante restaurante = new Restaurante();
            restaurante.setIdRestaurante(restauranteId);

            Horario horarioParam = new Horario();
            horarioParam.setIdHorario(2L);
            horarioParam.setHoraInicio(LocalTime.of(9, 0));
            horarioParam.setHoraFim(LocalTime.of(18, 0));
            horarioParam.setData(LocalDate.now());

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.of(restaurante));
            when(horarioRepository.existsById(horarioParam.getIdHorario())).thenReturn(false);
            when(horarioRepository.save(any(Horario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Horario horarioSalvo = horarioService.adicionarHorarioDeFuncionamentoAoRestaurante(restauranteId, horarioParam);

            // Assert
            assertNotNull(horarioSalvo);
            assertEquals(restaurante, horarioSalvo.getRestaurante());
            verify(restauranteRepository).findById(restauranteId);
            verify(horarioRepository).existsById(horarioParam.getIdHorario());
            verify(horarioRepository).save(any(Horario.class));
        }
        @Test
        void deveLancarExcecao_SeRestauranteNaoForEncontrado() {
            // Arrange
            Long restauranteId = 1L;
            Horario horarioParam = new Horario();
            horarioParam.setIdHorario(2L);

            when(restauranteRepository.findById(restauranteId)).thenReturn(Optional.empty());

            // Assert
            RegistroNotFoundException exception = assertThrows(RegistroNotFoundException.class,
                    () -> horarioService.adicionarHorarioDeFuncionamentoAoRestaurante(restauranteId, horarioParam));

            assertEquals("Restaurante não encontrado com ID: " + restauranteId, exception.getMessage());
            verify(restauranteRepository).findById(restauranteId);
            verify(horarioRepository, never()).save(any());
        }
    }

    @Nested
    class atualizarHorario{
        @Test
        void deveAtualizarHorarioComSucesso() {
            // Arrange
            Long idHorario = 1L;
            Horario horarioExistente = new Horario();
            horarioExistente.setIdHorario(idHorario);
            horarioExistente.setHoraInicio(LocalTime.of(9, 0));
            horarioExistente.setHoraFim(LocalTime.of(12, 0));

            Horario horarioAtualizado = new Horario();
            horarioAtualizado.setHoraInicio(LocalTime.of(10, 0));
            horarioAtualizado.setHoraFim(LocalTime.of(14, 0));

            when(horarioRepository.findById(idHorario)).thenReturn(Optional.of(horarioExistente));
            when(horarioRepository.save(any(Horario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Horario horarioSalvo = horarioService.atualizarHorario(idHorario, horarioAtualizado);

            // Assert
            assertNotNull(horarioSalvo);
            assertEquals(LocalTime.of(10, 0), horarioSalvo.getHoraInicio());
            assertEquals(LocalTime.of(14, 0), horarioSalvo.getHoraFim());
            verify(horarioRepository).findById(idHorario);
            verify(horarioRepository).save(any(Horario.class));
        }

        @Test
        void deveLancarExcecao_SeAtualizarParaQuantidadeMenorQueJaReservada() {
            // Arrange
            Long idHorario = 1L;
            Horario horarioExistente = new Horario();
            horarioExistente.setIdHorario(idHorario);
            horarioExistente.setQtdReservados(5);
            horarioExistente.setEspacosParaReserva(10);

            when(horarioRepository.findById(idHorario)).thenReturn(Optional.of(horarioExistente));

            // Act
            QuantidadeDeReservasException exception = assertThrows(QuantidadeDeReservasException.class,
                    () -> horarioService.atualizarQuantidadeDeReservasMaximas(idHorario, 4));

            assertEquals("Não é possível alterar numero de reservas para 4 pois já existem [5] reservadas realizadas!",
                    exception.getMessage());
            verify(horarioRepository).findById(idHorario);
            verify(horarioRepository, never()).save(any(Horario.class));
        }

        @Test
        void deveIncrementarReservas_ComSucesso() {
            // Arrange
            Long idHorario = 1L;
            Horario horarioExistente = new Horario();
            horarioExistente.setIdHorario(idHorario);
            horarioExistente.setQtdReservados(2);

            when(horarioRepository.findById(idHorario)).thenReturn(Optional.of(horarioExistente));
            when(horarioRepository.save(any(Horario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Horario horarioAtualizado = horarioService.incrementaQuantidadeReservas(idHorario);

            // Assert
            assertNotNull(horarioAtualizado);
            assertEquals(3, horarioAtualizado.getQtdReservados());
            verify(horarioRepository).findById(idHorario);
            verify(horarioRepository).save(any(Horario.class));
        }
    }
}
