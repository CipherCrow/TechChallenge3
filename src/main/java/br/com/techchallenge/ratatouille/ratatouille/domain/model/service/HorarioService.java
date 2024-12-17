package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Horario;

public interface HorarioService {

    public Horario adicionarHorarioDeFuncionamentoAoRestaurante(Long restauranteId, Horario horario);

    public Horario buscarPeloId(Long idHorario);

    public Horario atualizarHorario(Long idHorario, Horario horario);

    public Horario atualizarQuantidadeDeReservasMaximas(Long idHorario, Integer qtdReservasDesejado);

    public Horario incrementaQuantidadeReservas(Long idHorario);

    public Horario decrementaQuantidadeReservas(Long idHorario);

    public void deletarPeloId(Long idHorario);
}
