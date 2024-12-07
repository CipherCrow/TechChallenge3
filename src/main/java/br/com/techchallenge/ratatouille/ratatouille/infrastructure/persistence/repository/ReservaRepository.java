package br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

}
