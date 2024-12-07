package br.com.techchallenge.ratatouille.ratatouille.adapter.dto;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;

public record AvaliacaoDTO (
    Long idAvaliacao,
    Restaurante restaurante,
    String comentario,
    Integer estrelas,
    Usuario usuario
){}
