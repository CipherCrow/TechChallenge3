package br.com.techchallenge.ratatouille.ratatouille.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
public class Usuario {
    @Id
    private Long idUsuario;
    @NotEmpty(message = "Nome não pode estar vazio!")
    private String nome;
    @NotEmpty(message = "Email deve ser preenchido!")
    private String email;
    private int idade;
    private String sexo;
}
