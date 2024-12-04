package br.com.techchallenge.ratatouille.ratatouille.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Localizacao {
    @Id
    private Long idLocalizacao;

    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
}
