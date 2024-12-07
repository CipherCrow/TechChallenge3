package br.com.techchallenge.ratatouille.ratatouille.service.mapper;


import br.com.techchallenge.ratatouille.ratatouille.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto.HorarioDTO;

public class HorarioMapper {

    private HorarioMapper(){
        throw new IllegalStateException("Classe de utilidade");
    }

    public static HorarioDTO toDTO(Horario horario) {
        return new HorarioDTO(
                horario.getIdHorario(),
                horario.getHoraInicio(),
                horario.getHoraFim(),
                horario.getData(),
                horario.getEspacosParaReserva(),
                horario.getRestaurante(),
                horario.getQtdReservados()
        );
    }

    public static Horario toEntity(HorarioDTO dto) {
        Horario horario = new Horario();
        horario.setIdHorario(dto.idHorario());
        horario.setHoraInicio(dto.horaInicio());
        horario.setHoraFim(dto.horaFim());
        horario.setData(dto.data());
        horario.setEspacosParaReserva(dto.espacosParaReserva());
        horario.setRestaurante(dto.restaurante());
        horario.setQtdReservados(dto.qtdReservados());

        return horario;
    }
}