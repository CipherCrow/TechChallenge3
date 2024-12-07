package br.com.techchallenge.ratatouille.ratatouille.service;

import br.com.techchallenge.ratatouille.ratatouille.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.model.entities.Reserva;
import br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto.ReservaDTO;
import br.com.techchallenge.ratatouille.ratatouille.model.enums.StatusReservaEnum;
import br.com.techchallenge.ratatouille.ratatouille.repository.ReservaRepository;
import br.com.techchallenge.ratatouille.ratatouille.service.mapper.ReservaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ReservaService {

    private static final Logger log = LoggerFactory.getLogger(ReservaService.class);
    private static String idNotNull = "ID não pode ser nulo";

    // Sonar Lint Detectou perigo de injeção
    @Autowired
    private ReservaRepository reservaRepository;

    public Reserva criar(ReservaDTO reservaDTO) {
        log.info("Criando reserva ID: {}",reservaDTO.idReserva());
        Long parametroID = reservaDTO.idReserva();

        if (reservaRepository.existsById(parametroID)) {
            log.info("ID ja existe. ID: {}", parametroID);
            throw new IllegalArgumentException("ID já existe");
        }

        Reserva reserva = ReservaMapper.toEntity(reservaDTO);
        return reservaRepository.save(reserva);
    }

    public Reserva buscarPeloId(Long idReserva) {
        Objects.requireNonNull(idReserva, idNotNull);

        return reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RegistroNotFoundException("Reserva",idReserva));
    }

    public Reserva atualizarReserva(ReservaDTO reservaDTO) {
        Objects.requireNonNull(reservaDTO.idReserva(), idNotNull);

        Reserva reserva = this.buscarPeloId(reservaDTO.idReserva());
        reserva.setIdReserva(reservaDTO.idReserva());
        reserva.setStatus(reservaDTO.statusReserva());
        reserva.setCliente(reservaDTO.usuario());
        reserva.setHorario(reservaDTO.horario());

        return reservaRepository.save(reserva);
    }

    /***
     *
     * Definido pela regra de negocio que não deletaremos reservas da base para manter o registro.
     * O delete lógico acontecerá pelo status.
     *
     * ***/
    public Reserva cancelarPeloId(Long idReserva) {
        Objects.requireNonNull(idReserva, idNotNull);

        Reserva reserva = this.buscarPeloId(idReserva);
        reserva.setStatus(StatusReservaEnum.CANCELADA);
        return reservaRepository.save(reserva);
    }

    public Reserva finalizarPeloId(Long idReserva) {
        Objects.requireNonNull(idReserva, idNotNull);

        Reserva reserva = this.buscarPeloId(idReserva);
        reserva.setStatus(StatusReservaEnum.FINALIZADA);
        return reservaRepository.save(reserva);
    }

    public Reserva iniciarPeloId(Long idReserva) {
        Objects.requireNonNull(idReserva, idNotNull);

        Reserva reserva = this.buscarPeloId(idReserva);
        reserva.setStatus(StatusReservaEnum.ATIVA);
        return reservaRepository.save(reserva);
    }
}
