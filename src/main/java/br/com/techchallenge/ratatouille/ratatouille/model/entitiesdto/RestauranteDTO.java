package br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto;

import br.com.techchallenge.ratatouille.ratatouille.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.model.enums.TipoDeCozinhaEnum;

import java.util.List;

public record RestauranteDTO (
    Long idRestaurante,
    String nome,
    Localizacao localizacao,
    TipoDeCozinhaEnum tipoDeCozinha,
    List<Horario> horariosDeFuncionamento
){}
