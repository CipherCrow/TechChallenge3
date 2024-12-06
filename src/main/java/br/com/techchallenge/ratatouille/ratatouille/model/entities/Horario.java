package br.com.techchallenge.ratatouille.ratatouille.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHorario;

    @Column(nullable = false)
    @NotEmpty(message = "Deve existir hora de inicio!")
    private LocalTime horaInicio;

    @Column(nullable = false)
    @NotEmpty(message = "Deve existir hora de Fim!")
    private LocalTime horaFim;

    @Column(nullable = false)
    @NotEmpty(message = "Deve existir quantidade de reservas!")
    private int espacosParaReserva;

    @ManyToOne
    @JoinColumn(name = "idRestaurante", nullable = false)
    private Restaurante restaurante;
}
