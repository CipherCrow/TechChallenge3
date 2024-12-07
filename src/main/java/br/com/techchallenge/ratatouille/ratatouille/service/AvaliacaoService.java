package br.com.techchallenge.ratatouille.ratatouille.service;

import br.com.techchallenge.ratatouille.ratatouille.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.model.entities.Avaliacao;
import br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto.AvaliacaoDTO;
import br.com.techchallenge.ratatouille.ratatouille.repository.AvaliacaoRepository;
import br.com.techchallenge.ratatouille.ratatouille.service.mapper.AvaliacaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AvaliacaoService {

    private static final Logger log = LoggerFactory.getLogger(AvaliacaoService.class);
    private static String idNotNull = "ID não pode ser nulo";

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public Avaliacao criar(AvaliacaoDTO avaliacaoDTO) {
        log.info("Criando Avaliacao ID: {}",avaliacaoDTO.idAvaliacao());
        Long parametroID = avaliacaoDTO.idAvaliacao();

        if (avaliacaoRepository.existsById(parametroID)) {
            log.info("ID ja existe. ID: {}", parametroID);
            throw new IllegalArgumentException("ID já existe");
        }

        Avaliacao avaliacao = AvaliacaoMapper.toEntity(avaliacaoDTO);
        return avaliacaoRepository.save(avaliacao);
    }

    public Avaliacao buscarPeloId(Long idAvaliacao) {
        Objects.requireNonNull(idAvaliacao, idNotNull);

        return avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new RegistroNotFoundException("Avaliacao",idAvaliacao));
    }

    public List<Avaliacao> buscarListaAvaliacoesDoUsuario(Long idUsuario) {
        Objects.requireNonNull(idUsuario, idNotNull);

        return avaliacaoRepository.findByUsuario_IdUsuario(idUsuario);
    }

    public List<Avaliacao> buscarListaAvaliacoesDoRestaurante(Long idRestaurante) {
        Objects.requireNonNull(idRestaurante, idNotNull);

        return avaliacaoRepository.findByRestaurante_IdRestaurante(idRestaurante);
    }

}
