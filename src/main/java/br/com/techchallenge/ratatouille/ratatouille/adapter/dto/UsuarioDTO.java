package br.com.techchallenge.ratatouille.ratatouille.adapter.dto;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.SexoUsuarioEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.UsuarioStatusEnum;

public record UsuarioDTO (
        Long idUsuario,
        String nome,
        String email,
        Integer idade,
        SexoUsuarioEnum sexo,
        UsuarioStatusEnum status
){}
