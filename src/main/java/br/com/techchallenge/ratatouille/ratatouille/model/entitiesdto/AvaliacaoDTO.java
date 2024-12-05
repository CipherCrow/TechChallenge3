package br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto;

import br.com.techchallenge.ratatouille.ratatouille.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.model.entities.Usuario;

public record AvaliacaoDTO (
    Long id,
    Restaurante restaurante,
    String comentario,
    int estrelas,
    Usuario usuario
){}
