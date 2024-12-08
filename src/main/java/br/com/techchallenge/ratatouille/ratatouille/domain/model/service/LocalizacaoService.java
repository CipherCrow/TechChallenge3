package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.LocalizacaoDTO;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.LocalizacaoRepository;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.LocalizacaoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LocalizacaoService {

    private static final Logger log = LoggerFactory.getLogger(LocalizacaoService.class);
    private static String idNotNull = "ID não pode ser nulo";

    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    public Localizacao criar(LocalizacaoDTO localizacaoDTO) {
        log.info("Criando localizacao ID: {}",localizacaoDTO.idLocalizacao());
        Long parametroID = localizacaoDTO.idLocalizacao();

        if (localizacaoRepository.existsById(parametroID)) {
            log.info("ID ja existe. ID: {}", parametroID);
            throw new IdJaExistenteException("Id da localizacao já existente!");
        }

        Localizacao localizacao = LocalizacaoMapper.toEntity(localizacaoDTO);
        return localizacaoRepository.save(localizacao);
    }

    public Localizacao buscarPeloId(Long idLocalizacao) {
        Objects.requireNonNull(idLocalizacao, idNotNull);

        return localizacaoRepository.findById(idLocalizacao)
                .orElseThrow(() -> new RegistroNotFoundException("Localização",idLocalizacao));
    }

    public Localizacao atualizar(LocalizacaoDTO localizacaoDTO) {
        Objects.requireNonNull(localizacaoDTO.idLocalizacao(), idNotNull);

        Localizacao localizacao = this.buscarPeloId(localizacaoDTO.idLocalizacao());
        localizacao.setRua(localizacaoDTO.rua());
        localizacao.setCidade(localizacaoDTO.cidade());
        localizacao.setEstado(localizacaoDTO.estado());
        localizacao.setNumero(localizacaoDTO.numero());
        localizacao.setBairro(localizacaoDTO.bairro());

        return localizacaoRepository.save(localizacao);
    }
}
