package br.com.techchallenge.ratatouille.ratatouille.model.entities;

import br.com.techchallenge.ratatouille.ratatouille.model.enums.UsuarioStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotEmpty(message = "Nome não pode estar vazio!")
    private String nome;

    @NotEmpty(message = "Email deve ser preenchido!")
    private String email;

    private int idade;
    private String sexo;

    @Enumerated(EnumType.STRING)
    private UsuarioStatusEnum status = UsuarioStatusEnum.ATIVO;
}
