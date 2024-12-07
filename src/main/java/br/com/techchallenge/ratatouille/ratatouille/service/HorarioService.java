package br.com.techchallenge.ratatouille.ratatouille.service;

import br.com.techchallenge.ratatouille.ratatouille.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto.HorarioDTO;
import br.com.techchallenge.ratatouille.ratatouille.repository.HorarioRepository;
import br.com.techchallenge.ratatouille.ratatouille.service.mapper.HorarioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class HorarioService {

    private static final Logger log = LoggerFactory.getLogger(HorarioService.class);
    private static String idNotNull = "ID não pode ser nulo";

    // Sonar Lint Detectou perigo de injeção
    @Autowired
    private HorarioRepository horarioRepository;

    public Horario criar(HorarioDTO horarioDTO) {
        log.info("Criando Horário ID: {}", horarioDTO.idHorario());
        Long parametroID = horarioDTO.idHorario();

        if (horarioRepository.existsById(parametroID)) {
            log.info("ID ja existe. ID: {}", parametroID);
            throw new IllegalArgumentException("ID já existe");
        }

        Horario horario = HorarioMapper.toEntity(horarioDTO);
        return horarioRepository.save(horario);
    }

    public Horario buscarPeloId(Long idHorario) {
        Objects.requireNonNull(idHorario, idNotNull);

        return horarioRepository.findById(idHorario)
                .orElseThrow(() -> new RegistroNotFoundException("Horario",idHorario));
    }

    public Horario atualizarHorarios(HorarioDTO horarioDTO) {
        Objects.requireNonNull(horarioDTO.idHorario(), idNotNull);

        Horario horario = this.buscarPeloId(horarioDTO.idHorario());
        horario.setHoraFim(horarioDTO.horaFim());
        horario.setHoraInicio(horarioDTO.horaInicio());
        horario.setData(horarioDTO.data());

        return horarioRepository.save(horario);
    }

    public Horario atualizarQuantidadeDeReservas(HorarioDTO horarioDTO) {
        Objects.requireNonNull(horarioDTO.idHorario(), idNotNull);

        Horario horario = this.buscarPeloId(horarioDTO.idHorario());
        horario.setHoraFim(horarioDTO.horaFim());
        horario.setHoraInicio(horarioDTO.horaInicio());
        horario.setData(horarioDTO.data());

        return horarioRepository.save(horario);
    }

    public void deletarPeloId(Long idUsuario) {
        Objects.requireNonNull(idUsuario, idNotNull);
        horarioRepository.deleteById(idUsuario);
    }
}
