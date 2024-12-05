package br.com.techchallenge.ratatouille.ratatouille.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHorario;

    @NotEmpty(message = "Deve existir hora de inicio!")
    private LocalTime horaInicio;

    @NotEmpty(message = "Deve existir hora de Fim!")
    private LocalTime horaFim;

    @NotEmpty(message = "Deve existir quantidade de reservas!")
    private int espacosParaReserva;
}
