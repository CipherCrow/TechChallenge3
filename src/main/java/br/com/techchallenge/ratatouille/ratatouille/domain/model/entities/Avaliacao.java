package br.com.techchallenge.ratatouille.ratatouille.domain.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Avaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAvaliacao;

    @OneToOne
    @JoinColumn(name = "idRestaurante", nullable = false)
    private Restaurante restaurante;

    private String comentario;
    @NotNull(message = "Entre 1 à 5 estrelas devem ser dadas!")
    private Integer estrelas;

    @OneToOne
    @JoinColumn(name = "IdUsuario", nullable = false)
    private Usuario usuario;

}
