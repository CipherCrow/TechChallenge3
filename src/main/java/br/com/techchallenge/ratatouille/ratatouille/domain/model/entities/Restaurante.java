package br.com.techchallenge.ratatouille.ratatouille.domain.model.entities;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.TipoDeCozinhaEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "tb_restaurantes")
@AllArgsConstructor
@NoArgsConstructor
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRestaurante;

    @NotEmpty(message = "Nome não pode estar vazio!")
    @Column(nullable = false)
    private String nome;

    @OneToOne
    @JoinColumn(name = "idLocalizacao", nullable = false)
    private Localizacao localizacao;

    @Enumerated(EnumType.STRING)
    private TipoDeCozinhaEnum tipoDeCozinha;

}
