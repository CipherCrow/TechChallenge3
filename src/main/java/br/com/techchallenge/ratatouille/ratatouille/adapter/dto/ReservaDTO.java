package br.com.techchallenge.ratatouille.ratatouille.adapter.dto;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.StatusReservaEnum;

public record ReservaDTO(
    Long idReserva,
    StatusReservaEnum statusReserva,
    Usuario usuario,
    Horario horario
){}
