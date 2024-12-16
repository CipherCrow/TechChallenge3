package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegraDeNegocioException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Avaliacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AvaliacaoServiceImpl implements AvaliacaoService{

    private static String idNotNull = "ID não pode ser nulo";

    private final AvaliacaoRepository avaliacaoRepository;

    private final RestauranteService restauranteService;

    private final UsuarioService usuarioService;

    public Avaliacao criar(Long idRestaurante, Long idUsuario, Avaliacao avaliacaoParam) {
        Objects.requireNonNull(idRestaurante);
        Objects.requireNonNull(idUsuario);
        Objects.requireNonNull(avaliacaoParam);

        if (avaliacaoRepository.existsById(avaliacaoParam.getIdAvaliacao())) {
            throw new IdJaExistenteException("Id da avaliacao já existente!");
        }

        if(avaliacaoParam.getEstrelas() > 5 || avaliacaoParam.getEstrelas() < 0 ){
            throw new RegraDeNegocioException("Deve ser dado de 1 à 5 estrelas de avaliação!");
        }

        Restaurante restaurante = restauranteService.buscarPeloId(idRestaurante);
        Usuario usuario = usuarioService.buscarPeloId(idUsuario);

        avaliacaoParam.setIdAvaliacao(null);
        avaliacaoParam.setRestaurante(restaurante);
        avaliacaoParam.setUsuario(usuario);
        return avaliacaoRepository.save(avaliacaoParam);
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
