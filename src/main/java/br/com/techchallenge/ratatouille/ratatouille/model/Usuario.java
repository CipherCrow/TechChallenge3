package br.com.techchallenge.ratatouille.ratatouille.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Usuario {
    @Id
    private Long idUsuario;
    private String nome;
    private String email;
    private int idade;
    private String sexo;
}
