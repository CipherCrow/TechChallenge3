package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.TipoDeCozinhaEnum;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService{

    private static String idNotNull = "ID não pode ser nulo";

    private final RestauranteRepository restauranteRepository;

    @Autowired
    private LocalizacaoService localizacaoService;

    public Restaurante criar(Restaurante restauranteParam) {
        if (restauranteRepository.existsById(restauranteParam.getIdRestaurante())) {
            throw new IdJaExistenteException("Id do restaurante já existente!");
        }

        return restauranteRepository.save(restauranteParam);
    }

    public Restaurante buscarPeloId(Long idRestaurante) {
        Objects.requireNonNull(idRestaurante, idNotNull);

        return restauranteRepository.findById(idRestaurante)
                .orElseThrow(() ->
                        new RegistroNotFoundException("Restaurante", idRestaurante));
    }

    public Restaurante atualizarDados(Long idRestaurante,
                                      String nomeRestaurante,
                                      TipoDeCozinhaEnum tipoDeCozinhaEnum) {
        Objects.requireNonNull(idRestaurante, idNotNull);
        Objects.requireNonNull(nomeRestaurante, idNotNull);
        Objects.requireNonNull(tipoDeCozinhaEnum, idNotNull);

        Restaurante restaurante = this.buscarPeloId(idRestaurante);
        restaurante.setNome(nomeRestaurante);
        restaurante.setTipoDeCozinha(tipoDeCozinhaEnum);

        return restauranteRepository.save(restaurante);
    }

    public Restaurante atualizarLocalizacao(Long idRestaurante,
                                      Localizacao localizacao) {
        Objects.requireNonNull(idRestaurante, idNotNull);
        Objects.requireNonNull(localizacao, idNotNull);

        localizacaoService.atualizar(localizacao);
        return this.buscarPeloId(idRestaurante);
    }

    public List<Restaurante> buscarPeloNome(String nomeRestaurante) {
        Objects.requireNonNull(nomeRestaurante, idNotNull);
        return restauranteRepository.findByNomeLike(nomeRestaurante);
    }

    public List<Restaurante> buscarPelaLocalizacao(Localizacao localizacao) {
        Objects.requireNonNull(localizacao, idNotNull);
        return restauranteRepository.findByLocalizacao(
                localizacao.getEstado(),
                localizacao.getCidade(),
                localizacao.getBairro(),
                localizacao.getRua());
    }

}
