package br.com.techchallenge.ratatouille.ratatouille.domain.model.entities;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.UsuarioStatusEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.SexoUsuarioEnum;
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

    private Integer idade;

    @Enumerated(EnumType.STRING)
    private SexoUsuarioEnum sexo;

    @Enumerated(EnumType.STRING)
    private UsuarioStatusEnum status;
}
