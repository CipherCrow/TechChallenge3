package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Avaliacao;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.AvaliacaoDTO;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.AvaliacaoRepository;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.AvaliacaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AvaliacaoService {

    private static String idNotNull = "ID não pode ser nulo";

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private UsuarioService usuarioService;

    public Avaliacao criar(Long idRestaurante, Long idUsuario, AvaliacaoDTO avaliacaoDTO) {
        Objects.requireNonNull(idRestaurante);
        Objects.requireNonNull(idUsuario);
        Objects.requireNonNull(avaliacaoDTO);

        if (avaliacaoRepository.existsById(avaliacaoDTO.idAvaliacao())) {
            throw new IdJaExistenteException("Id da avaliacao já existente!");
        }

        Restaurante restaurante = restauranteService.buscarPeloId(idRestaurante);
        Usuario usuario = usuarioService.buscarPeloId(idUsuario);

        Avaliacao avaliacao = AvaliacaoMapper.toEntity(avaliacaoDTO);
        avaliacao.setRestaurante(restaurante);
        avaliacao.setUsuario(usuario);
        return avaliacaoRepository.save(avaliacao);
    }

    public Avaliacao buscarPeloId(Long idAvaliacao) {
        Objects.requireNonNull(idAvaliacao, idNotNull);

        return avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new RegistroNotFoundException("Avaliacao",idAvaliacao));
    }

    public List<Avaliacao> buscarTodasAvaliacoesRestaurante(Long idRestaurante) {
        Objects.requireNonNull(idRestaurante, idNotNull);

        Restaurante restaurante = restauranteService.buscarPeloId(idRestaurante);
        return avaliacaoRepository.findAvaliacaosByRestaurante(restaurante);
    }

}
