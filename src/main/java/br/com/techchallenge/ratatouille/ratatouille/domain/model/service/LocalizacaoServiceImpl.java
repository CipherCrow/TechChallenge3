package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.LocalizacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LocalizacaoServiceImpl implements LocalizacaoService{

    private static String idNotNull = "ID não pode ser nulo";

    private final LocalizacaoRepository localizacaoRepository;

    public Localizacao criar(Localizacao localizacaoParam) {
        //localizacaoParam.setIdLocalizacao(null);
        return localizacaoRepository.save(localizacaoParam);
    }

    public Localizacao buscarPeloId(Long idLocalizacao) {
        Objects.requireNonNull(idLocalizacao, idNotNull);

        return localizacaoRepository.findById(idLocalizacao)
                .orElseThrow(() -> new RegistroNotFoundException("Localização",idLocalizacao));
    }

    public Localizacao atualizar(Localizacao localizacaoParam) {
        Objects.requireNonNull(localizacaoParam.getIdLocalizacao(), idNotNull);

        Localizacao localizacao = this.buscarPeloId(localizacaoParam.getIdLocalizacao());
        localizacao.setRua(localizacaoParam.getRua());
        localizacao.setCidade(localizacaoParam.getCidade());
        localizacao.setEstado(localizacaoParam.getEstado());
        localizacao.setNumero(localizacaoParam.getNumero());
        localizacao.setBairro(localizacaoParam.getBairro());

        return localizacaoRepository.save(localizacao);
    }
}
