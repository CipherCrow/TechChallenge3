package br.com.techchallenge.ratatouille.ratatouille.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
public class Localizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLocalizacao;

    private String estado;

    @NotEmpty(message = "Cidade não pode estar vazio!")
    private String cidade;

    @NotEmpty(message = "Bairro pode estar vazio!")
    private String bairro;

    @NotEmpty(message = "Rua não pode estar vazio!")
    private String rua;

    @NotEmpty(message = "Numero não pode estar vazio!")
    private String numero;
}
