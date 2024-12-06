package br.com.techchallenge.ratatouille.ratatouille.model.entities;

import br.com.techchallenge.ratatouille.ratatouille.model.enums.UsuarioStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false)
    @NotEmpty(message = "Nome não pode estar vazio!")
    private String nome;

    @Column(nullable = false)
    @NotEmpty(message = "Email deve ser preenchido!")
    private String email;

    private int idade;
    private String sexo;

    @Enumerated(EnumType.STRING)
    private UsuarioStatusEnum status = UsuarioStatusEnum.ATIVO;
}
