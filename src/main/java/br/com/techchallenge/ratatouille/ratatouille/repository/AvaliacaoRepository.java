package br.com.techchallenge.ratatouille.ratatouille.repository;

import br.com.techchallenge.ratatouille.ratatouille.model.entities.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
}
