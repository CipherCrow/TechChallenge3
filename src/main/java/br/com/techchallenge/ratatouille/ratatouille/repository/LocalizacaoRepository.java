package br.com.techchallenge.ratatouille.ratatouille.repository;

import br.com.techchallenge.ratatouille.ratatouille.model.entities.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {

}
