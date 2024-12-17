package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;

public interface LocalizacaoService {

    Localizacao criar(Localizacao localizacao);
    Localizacao buscarPeloId(Long idLocalizacao);
    Localizacao atualizar(Localizacao localizacao);
}
