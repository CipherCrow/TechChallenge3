package br.com.techchallenge.ratatouille.ratatouille.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Localizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLocalizacao;

    private String estado;

    @Column(nullable = false)
    @NotEmpty(message = "Cidade não pode estar vazio!")
    private String cidade;

    @Column(nullable = false)
    @NotEmpty(message = "Bairro pode estar vazio!")
    private String bairro;

    @Column(nullable = false)
    @NotEmpty(message = "Rua não pode estar vazio!")
    private String rua;

    @Column(nullable = false)
    @NotEmpty(message = "Numero não pode estar vazio!")
    private String numero;
}
