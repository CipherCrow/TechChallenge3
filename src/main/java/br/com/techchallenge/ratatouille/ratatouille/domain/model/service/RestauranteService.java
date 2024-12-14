package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.TipoDeCozinhaEnum;

import java.util.List;


public interface RestauranteService {

    Restaurante criar(Restaurante restaurante) ;
    Restaurante buscarPeloId(Long idRestaurante) ;
    Restaurante atualizarDados(Long idRestaurante,
                                      String nomeRestaurante,
                                      TipoDeCozinhaEnum tipoDeCozinhaEnum) ;
    Restaurante atualizarLocalizacao(Long idRestaurante,
                                      Localizacao localizacao) ;
    List<Restaurante> buscarPeloNome(String nomeRestaurante) ;
    List<Restaurante> buscarPelaLocalizacao(Localizacao localizacao) ;

}
