package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Avaliacao;

import java.util.List;

public interface AvaliacaoService {

    Avaliacao criar(Long idRestaurante, Long idUsuario, Avaliacao avaliacao);
    Avaliacao buscarPeloId(Long idAvaliacao);
    List<Avaliacao> buscarTodasAvaliacoesRestaurante(Long idRestaurante);

}
