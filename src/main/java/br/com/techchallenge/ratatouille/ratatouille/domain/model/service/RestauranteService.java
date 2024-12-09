package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.LocalizacaoDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.RestauranteDTO;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.TipoDeCozinhaEnum;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.RestauranteRepository;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.RestauranteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RestauranteService {

    private static String idNotNull = "ID não pode ser nulo";

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private LocalizacaoService localizacaoService;

    public Restaurante criar(RestauranteDTO restauranteDTO) {
        Long parametroID = restauranteDTO.idRestaurante();

        if (restauranteRepository.existsById(parametroID)) {
            throw new IdJaExistenteException("Id do restaurante já existente!");
        }

        Restaurante restaurante = RestauranteMapper.toEntity(restauranteDTO);
        return restauranteRepository.save(restaurante);
    }

    public Restaurante buscarPeloId(Long idRestaurante) {
        Objects.requireNonNull(idRestaurante, idNotNull);

        return restauranteRepository.findById(idRestaurante)
                .orElseThrow(() ->
                        new RegistroNotFoundException("Restaurante", idRestaurante));
    }

    public Restaurante atualizarDados(Long idRestaurante,
                                      String nomeRestaurante,
                                      TipoDeCozinhaEnum tipoDeCozinhaEnum,
                                      Localizacao localizacao) {
        Objects.requireNonNull(idRestaurante, idNotNull);
        Objects.requireNonNull(nomeRestaurante, idNotNull);
        Objects.requireNonNull(tipoDeCozinhaEnum, idNotNull);
        Objects.requireNonNull(localizacao, idNotNull);

        Restaurante restaurante = this.buscarPeloId(idRestaurante);
        restaurante.setNome(nomeRestaurante);
        restaurante.setTipoDeCozinha(tipoDeCozinhaEnum);

        return restauranteRepository.save(restaurante);
    }

    public Restaurante atualizarLocalizacao(Long idRestaurante,
                                      LocalizacaoDTO localizacao) {
        Objects.requireNonNull(idRestaurante, idNotNull);
        Objects.requireNonNull(localizacao, idNotNull);

        localizacaoService.atualizar(localizacao);
        Restaurante restaurante = this.buscarPeloId(idRestaurante);

        return restaurante;
    }

    public List<Restaurante> buscarPeloNome(String nomeRestaurante) {
        Objects.requireNonNull(nomeRestaurante, idNotNull);
        return restauranteRepository.findByNomeLike(nomeRestaurante);
    }

    public List<Restaurante> buscarPelaLocalizacao(LocalizacaoDTO localizacaoDTO) {
        Objects.requireNonNull(localizacaoDTO, idNotNull);
        return restauranteRepository.findByLocalizacao(
                localizacaoDTO.estado(),
                localizacaoDTO.cidade(),
                localizacaoDTO.bairro(),
                localizacaoDTO.rua());
    }

}
