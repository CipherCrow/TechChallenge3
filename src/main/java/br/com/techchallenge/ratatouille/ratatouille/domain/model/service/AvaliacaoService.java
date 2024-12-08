package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Avaliacao;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.AvaliacaoDTO;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.AvaliacaoRepository;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.AvaliacaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new IdJaExistenteException("Id da avaliacao já existente!");
        }

        Avaliacao avaliacao = AvaliacaoMapper.toEntity(avaliacaoDTO);
        return avaliacaoRepository.save(avaliacao);
    }

    public Avaliacao buscarPeloId(Long idAvaliacao) {
        Objects.requireNonNull(idAvaliacao, idNotNull);

        return avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new RegistroNotFoundException("Avaliacao",idAvaliacao));
    }

}
