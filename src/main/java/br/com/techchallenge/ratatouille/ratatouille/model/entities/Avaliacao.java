package br.com.techchallenge.ratatouille.ratatouille.model.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Avaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "idRestaurante", referencedColumnName = "idRestaurante")
    private Restaurante restaurante;

    private String comentario;
    private int estrelas;

    @OneToOne
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    private Usuario usuario;
}
