package br.com.techchallenge.ratatouille.ratatouille.adapter.dto;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.TipoDeCozinhaEnum;

import java.util.List;

public record RestauranteDTO(
    Long idRestaurante,
    String nome,
    Localizacao localizacao,
    TipoDeCozinhaEnum tipoDeCozinhaEnum,
    List<Horario> horariosDeFuncionamento
){}
