package br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto;

import br.com.techchallenge.ratatouille.ratatouille.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.model.enums.StatusReservaEnum;

public record ReservaDTO(
    Long idReserva,
    StatusReservaEnum statusReserva,
    Usuario usuario,
    Horario horario
){}
