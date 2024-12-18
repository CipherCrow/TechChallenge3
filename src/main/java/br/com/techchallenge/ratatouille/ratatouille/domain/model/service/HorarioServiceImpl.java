package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.QuantidadeDeReservasException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.HorarioRepository;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HorarioServiceImpl implements  HorarioService{

    private static String idNotNull = "ID não pode ser nulo";

    // Sonar Lint Detectou perigo de injeção
    private final HorarioRepository horarioRepository;

    private final RestauranteRepository restauranteRepository;

    public Horario adicionarHorarioDeFuncionamentoAoRestaurante(Long restauranteId, Horario horarioParam) {
        // Buscar o restaurante
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new RegistroNotFoundException("Restaurante", restauranteId));

        if(horarioRepository.existsById(horarioParam.getIdHorario())){
           throw new IdJaExistenteException("Id do Horario já existente!");
        }

        horarioParam.setIdHorario(null);
        // Associar o restaurante ao horário
        horarioParam.setRestaurante(restaurante);
        horarioParam.setQtdReservados(0);

        // Salvar o horário
        return horarioRepository.save(horarioParam);
    }

    public Horario buscarPeloId(Long idHorario) {
        Objects.requireNonNull(idHorario, idNotNull);

        return horarioRepository.findById(idHorario)
                .orElseThrow(() -> new RegistroNotFoundException("Horario",idHorario));
    }

    public Horario atualizarHorario(Long idHorario, Horario horarioParam) {
        Objects.requireNonNull(idHorario, idNotNull);

        Horario horario = this.buscarPeloId(idHorario);
        horario.setData(horarioParam.getData());
        horario.setHoraInicio(horarioParam.getHoraInicio());
        horario.setHoraFim(horarioParam.getHoraFim());

        return horarioRepository.save(horario);
    }

    public Horario atualizarQuantidadeDeReservasMaximas(Long idHorario, Integer qtdReservasDesejado) {
        Objects.requireNonNull(idHorario, idNotNull);

        Horario horario = this.buscarPeloId(idHorario);

        Integer jaReservados = horario.getQtdReservados();

        if(qtdReservasDesejado <= jaReservados){
            throw new QuantidadeDeReservasException
                    ("Não é possível alterar numero de reservas para " + qtdReservasDesejado
                            + " pois já existem [" + jaReservados + "] reservadas realizadas!");
        }

        horario.setEspacosParaReserva(qtdReservasDesejado);
        return horarioRepository.save(horario);
    }

    public Horario incrementaQuantidadeReservas(Long idHorario) {
        Objects.requireNonNull(idHorario, idNotNull);

        Horario horario = this.buscarPeloId(idHorario);
        horario.setQtdReservados(horario.getQtdReservados() + 1);
        return horarioRepository.save(horario);
    }

    public Horario decrementaQuantidadeReservas(Long idHorario) {
        Objects.requireNonNull(idHorario, idNotNull);

        Horario horario = this.buscarPeloId(idHorario);
        horario.setQtdReservados(horario.getQtdReservados() - 1);
        return horarioRepository.save(horario);
    }

    public void deletarPeloId(Long idHorario) {
        Objects.requireNonNull(idHorario, idNotNull);

        Horario horario = this.buscarPeloId(idHorario);

        if(horario.getQtdReservados() != 0){
            throw new QuantidadeDeReservasException
                    ("Não é possível deletar horarios que já possuem reservas validas!");
        }

        horarioRepository.deleteById(idHorario);
    }
}
