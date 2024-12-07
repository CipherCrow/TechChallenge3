package br.com.techchallenge.ratatouille.ratatouille.model.entities;

import br.com.techchallenge.ratatouille.ratatouille.model.enums.TipoDeCozinhaEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "tb_restaurantes")
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

    @OneToMany(
            mappedBy = "restaurante",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Horario> horariosDeFuncionamento;
}
