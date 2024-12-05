package br.com.techchallenge.ratatouille.ratatouille.service;

import br.com.techchallenge.ratatouille.ratatouille.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto.LocalizacaoDTO;
import br.com.techchallenge.ratatouille.ratatouille.repository.LocalizacaoRepository;
import br.com.techchallenge.ratatouille.ratatouille.service.mapper.LocalizacaoMapper;
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

    public Localizacao criarLocalizacao(LocalizacaoDTO localizacaoDTO) {
        log.info("Criando localizacao ID: {}",localizacaoDTO.idLocalizacao());
        Long parametroID = localizacaoDTO.idLocalizacao();

        if (localizacaoRepository.existsById(parametroID)) {
            log.info("ID ja existe. ID: {}", parametroID);
            throw new IllegalArgumentException("ID já existe");
        }

        Localizacao Localizacao = LocalizacaoMapper.toEntity(localizacaoDTO);
        return localizacaoRepository.save(Localizacao);
    }

    public Localizacao buscarLocalizacao(Long idLocalizacao) {
        Objects.requireNonNull(idLocalizacao, idNotNull);

        return localizacaoRepository.findById(idLocalizacao)
                .orElseThrow(() -> new RegistroNotFoundException("Localização",idLocalizacao));
    }

    public Localizacao atualizarLocalizacao(LocalizacaoDTO localizacaoDTO) {
        Objects.requireNonNull(localizacaoDTO.idLocalizacao(), idNotNull);

        Localizacao localizacao = this.buscarLocalizacao(localizacaoDTO.idLocalizacao());
        localizacao.setRua(localizacaoDTO.rua());
        localizacao.setCidade(localizacaoDTO.cidade());
        localizacao.setEstado(localizacaoDTO.estado());
        localizacao.setNumero(localizacaoDTO.numero());
        localizacao.setBairro(localizacaoDTO.bairro());

        return localizacaoRepository.save(localizacao);
    }
}
