package br.com.techchallenge.ratatouille.ratatouille.model;

import br.com.techchallenge.ratatouille.ratatouille.model.enums.TipoDeCozinhaEnum;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "tb_restaurantes")
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRestaurante;

    private String nome;

    @OneToOne
    @JoinColumn(name = "tb_localizacao_id")
    private Localizacao localizacao;

    @Enumerated(EnumType.STRING)
    private TipoDeCozinhaEnum tipoDeCozinha;

    @OneToMany(
            mappedBy = "restaurante",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Horario> horariosDeFuncionamento;
}
