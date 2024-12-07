package br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto;

import br.com.techchallenge.ratatouille.ratatouille.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.model.entities.Usuario;

public record AvaliacaoDTO (
    Long idAvaliacao,
    Restaurante restaurante,
    String comentario,
    Integer estrelas,
    Usuario usuario
){}
