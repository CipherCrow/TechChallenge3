package br.com.techchallenge.ratatouille.ratatouille.adapter.mapper;


import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Reserva;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.ReservaDTO;

public class ReservaMapper {

    private ReservaMapper(){
        throw new IllegalStateException("Classe de utilidade");
    }

    public static ReservaDTO toDTO(Reserva reserva) {
        return new ReservaDTO(
                reserva.getIdReserva(),
                reserva.getStatus(),
                reserva.getCliente(),
                reserva.getHorario()
        );
    }

    public static Reserva toEntity(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setIdReserva(dto.idReserva());
        reserva.setStatus(dto.statusReserva());
        reserva.setCliente(dto.usuario());
        reserva.setHorario(dto.horario());
        return reserva;
    }
}