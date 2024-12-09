package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegraDeNegocioException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Reserva;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.ReservaDTO;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.StatusReservaEnum;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.ReservaRepository;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.ReservaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ReservaService {

    private static String idNotNull = "ID não pode ser nulo";

    // Sonar Lint Detectou perigo de injeção
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private HorarioService horarioService;

    public Reserva adicionarReservaParaHorario(Long idHorario,ReservaDTO reservaDTO) {
        Objects.requireNonNull(idHorario, idNotNull);

        // Buscar o Horario
        Horario horario = horarioService.buscarPeloId(idHorario);

        if(reservaRepository.existsById(reservaDTO.idReserva())){
            new IdJaExistenteException("Id da reserva já existente!");
        }

        if(horario.getQtdReservados() == horario.getEspacosParaReserva()){
            new RegraDeNegocioException("Não existem mais espaços nesse horário!");
        }

        // Associar o horario á reserva
        Reserva reserva = ReservaMapper.toEntity(reservaDTO);
        reserva.setHorario(horario);

        Reserva reservaSalva = reservaRepository.save(reserva);

        horarioService.incrementaQuantidadeReservas(horario.getIdHorario());

        return reservaSalva;
    }

    public Reserva buscarPeloId(Long idReserva) {
        Objects.requireNonNull(idReserva, idNotNull);

        return reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RegistroNotFoundException("Reserva",idReserva));
    }

    public List<Reserva> buscarTodasReservasDoHorario(Long idHorario) {
        Objects.requireNonNull(idHorario, idNotNull);
        return reservaRepository.findReservasByHorario_IdHorario(idHorario);
    }
    /***
     *
     * Será usado pelo pessoal do restaurante para visualizar as reservas
     *
     * ***/
    public List<Reserva> buscarTodasReservasDoHorarioComStatus(Long idHorario, StatusReservaEnum status) {
        Objects.requireNonNull(idHorario, idNotNull);
        Objects.requireNonNull(status, idNotNull);

        return reservaRepository.findReservasByHorario_IdHorarioAndStatus(idHorario, status.name());
    }

    /***
     *
     * Definido pela regra de negocio que não deletaremos reservas da base para manter o registro.
     * O delete lógico acontecerá pelo status.
     *
     * Quando uma reserva é cancelada, a quantidade de reservas é reduzida.
     * Note que isso não deve acontecer para quando ela é finalizada.
     *
     * ***/
    public Reserva cancelarPeloId(Long idReserva) {
        Objects.requireNonNull(idReserva, idNotNull);

        Reserva reserva = this.buscarPeloId(idReserva);
        if (!reserva.getStatus().equals(StatusReservaEnum.RESERVADO)){
            throw new RegraDeNegocioException("Apenas pode se cancelar reservas que estejam RESERVADAS!");
        }
        reserva.setStatus(StatusReservaEnum.CANCELADA);

        Reserva reservaSalva = reservaRepository.save(reserva);

        horarioService.decrementaQuantidadeReservas(reserva.getHorario().getIdHorario());

        return reservaSalva;
    }

    public Reserva finalizarPeloId(Long idReserva) {
        Objects.requireNonNull(idReserva, idNotNull);

        Reserva reserva = this.buscarPeloId(idReserva);

        if (!reserva.getStatus().equals(StatusReservaEnum.ATIVA)){
            throw new RegraDeNegocioException("Apenas podem finalizar reservas que estejam ATIVAS!");
        }

        reserva.setStatus(StatusReservaEnum.FINALIZADA);
        return reservaRepository.save(reserva);
    }

    public Reserva iniciarPeloId(Long idReserva) {
        Objects.requireNonNull(idReserva, idNotNull);

        Reserva reserva = this.buscarPeloId(idReserva);

        if (!reserva.getStatus().equals(StatusReservaEnum.RESERVADO)){
            throw new RegraDeNegocioException("Apenas podem iniciar reservas que estejam RESERVADAS!");
        }

        reserva.setStatus(StatusReservaEnum.ATIVA);
        return reservaRepository.save(reserva);
    }
}
