package br.com.techchallenge.ratatouille.ratatouille.service.mapper;


import br.com.techchallenge.ratatouille.ratatouille.model.entities.Avaliacao;
import br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto.AvaliacaoDTO;

public class AvaliacaoMapper {

    private AvaliacaoMapper(){
        throw new IllegalStateException("Classe de utilidade");
    }

    public static AvaliacaoDTO toDTO(Avaliacao avaliacao) {
        return new AvaliacaoDTO(
                avaliacao.getIdAvaliacao(),
                avaliacao.getRestaurante(),
                avaliacao.getComentario(),
                avaliacao.getEstrelas(),
                avaliacao.getUsuario()

        );
    }

    public static Avaliacao toEntity(AvaliacaoDTO dto) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setIdAvaliacao(dto.idAvaliacao());
        avaliacao.setRestaurante(dto.restaurante());
        avaliacao.setComentario(dto.comentario());
        avaliacao.setEstrelas(dto.estrelas());
        avaliacao.setUsuario(dto.usuario());

        return avaliacao;
    }
}