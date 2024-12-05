package br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto;

import br.com.techchallenge.ratatouille.ratatouille.model.enums.UsuarioStatusEnum;

public record UsuarioDTO (
        Long idUsuario,
        String nome,
        String email,
        int idade,
        String sexo,
        UsuarioStatusEnum status
){}
