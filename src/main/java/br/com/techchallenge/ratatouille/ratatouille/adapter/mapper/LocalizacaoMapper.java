package br.com.techchallenge.ratatouille.ratatouille.adapter.mapper;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.LocalizacaoDTO;

public class LocalizacaoMapper {

    private LocalizacaoMapper(){
        throw new IllegalStateException("Classe de utilidade");
    }

    public static LocalizacaoDTO toDTO(Localizacao localizacao) {
        return new LocalizacaoDTO(
                localizacao.getIdLocalizacao(),
                localizacao.getEstado(),
                localizacao.getCidade(),
                localizacao.getBairro(),
                localizacao.getRua(),
                localizacao.getNumero()
        );
    }

    public static Localizacao toEntity(LocalizacaoDTO dto) {
        Localizacao localizacao = new Localizacao();
        localizacao.setIdLocalizacao(dto.idLocalizacao());
        localizacao.setEstado(dto.estado());
        localizacao.setCidade(dto.cidade());
        localizacao.setBairro(dto.bairro());
        localizacao.setRua(dto.rua());
        localizacao.setNumero(dto.numero());

        return localizacao;
    }
}