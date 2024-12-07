package br.com.techchallenge.ratatouille.ratatouille.service.mapper;

import br.com.techchallenge.ratatouille.ratatouille.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto.RestauranteDTO;

public class RestauranteMapper {

    private RestauranteMapper(){
        throw new IllegalStateException("Classe de utilidade");
    }

    public static RestauranteDTO toDTO(Restaurante restaurante) {
        return new RestauranteDTO(
                restaurante.getIdRestaurante(),
                restaurante.getNome(),
                restaurante.getLocalizacao(),
                restaurante.getTipoDeCozinha(),
                restaurante.getHorariosDeFuncionamento()
        );
    }

    public static Restaurante toEntity(RestauranteDTO dto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setIdRestaurante(dto.idRestaurante());
        restaurante.setNome(dto.nome());
        restaurante.setLocalizacao(dto.localizacao());
        restaurante.setTipoDeCozinha(dto.tipoDeCozinhaEnum());
        restaurante.setHorariosDeFuncionamento(dto.horariosDeFuncionamento());

        return restaurante;
    }
}