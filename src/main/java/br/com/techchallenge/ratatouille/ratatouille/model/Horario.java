package br.com.techchallenge.ratatouille.ratatouille.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
public class Horario {
    @Id
    private Long idHorario;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private int espacosParaReserva;
}
