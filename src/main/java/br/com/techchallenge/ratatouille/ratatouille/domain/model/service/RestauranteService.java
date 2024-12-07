package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.RestauranteDTO;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.TipoDeCozinhaEnum;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.RestauranteRepository;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.RestauranteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RestauranteService {

    private static final Logger log = LoggerFactory.getLogger(RestauranteService.class);
    private static String idNotNull = "ID não pode ser nulo";

    @Autowired
    private RestauranteRepository restauranteRepository;

    public Restaurante criar(RestauranteDTO restauranteDTO) {
        log.info("Criando Restaurante ID: {}",restauranteDTO.idRestaurante());
        Long parametroID = restauranteDTO.idRestaurante();

        if (restauranteRepository.existsById(parametroID)) {
            log.info("ID ja existe. ID: {}", parametroID);
            throw new IllegalArgumentException("ID já existe");
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
        restaurante.setLocalizacao(localizacao);

        return restauranteRepository.save(restaurante);
    }

}
