package br.com.techchallenge.ratatouille.ratatouille.adapter.dto;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;

import java.time.LocalDate;
import java.time.LocalTime;

public record HorarioDTO (
    Long idHorario,
    LocalTime horaInicio,
    LocalTime horaFim,
    LocalDate data,
    Integer espacosParaReserva,
    Restaurante restaurante,
    Integer qtdReservados
){}
