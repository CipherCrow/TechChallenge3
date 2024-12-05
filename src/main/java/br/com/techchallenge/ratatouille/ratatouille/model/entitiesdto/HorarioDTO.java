package br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto;

import java.time.LocalTime;

public record HorarioDTO (
    Long idHorario,
    LocalTime horaInicio,
    LocalTime horaFim,
    int espacosParaReserva
){}
