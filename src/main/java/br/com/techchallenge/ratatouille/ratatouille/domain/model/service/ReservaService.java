package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Reserva;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.StatusReservaEnum;

import java.util.List;

public interface ReservaService {

    Reserva adicionarReservaParaHorario(Long idHorario,Reserva reserva);
    Reserva buscarPeloId(Long idReserva);
    /***
     *
     * Será usado pelo pessoal do restaurante para visualizar as reservas
     *
     * ***/
    List<Reserva> buscarTodasReservasDoHorarioComStatus(Long idHorario, StatusReservaEnum status);
    List<Reserva> buscarTodasReservasDoHorario(Long idHorario);
    /***
     *
     * Definido pela regra de negocio que não deletaremos reservas da base para manter o registro.
     * O delete lógico acontecerá pelo status.
     *
     * Quando uma reserva é cancelada, a quantidade de reservas é reduzida.
     * Note que isso não deve acontecer para quando ela é finalizada.
     *
     * ***/
    Reserva cancelarPeloId(Long idReserva);
    Reserva finalizarPeloId(Long idReserva);
    Reserva iniciarPeloId(Long idReserva);
}
