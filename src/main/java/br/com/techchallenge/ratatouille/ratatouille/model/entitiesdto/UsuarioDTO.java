package br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto;

import br.com.techchallenge.ratatouille.ratatouille.model.enums.SexoUsuarioEnum;
import br.com.techchallenge.ratatouille.ratatouille.model.enums.UsuarioStatusEnum;

public record UsuarioDTO (
        Long idUsuario,
        String nome,
        String email,
        int idade,
        SexoUsuarioEnum sexo,
        UsuarioStatusEnum status
){}
