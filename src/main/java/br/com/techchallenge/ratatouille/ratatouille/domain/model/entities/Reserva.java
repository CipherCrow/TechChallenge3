package br.com.techchallenge.ratatouille.ratatouille.domain.model.entities;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.StatusReservaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reserva {
    @Id
    private Long idReserva;

    @Enumerated(EnumType.STRING)
    private StatusReservaEnum status;

    @OneToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "idHorario", referencedColumnName = "idHorario")
    private Horario horario;
}
