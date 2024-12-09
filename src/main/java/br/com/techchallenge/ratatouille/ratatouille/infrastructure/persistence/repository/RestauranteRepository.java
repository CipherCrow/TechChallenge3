package br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.TipoDeCozinhaEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    List<Restaurante> findByTipoDeCozinhaEquals(TipoDeCozinhaEnum tipoDeCozinhaEnum);

    @Query("SELECT r FROM Restaurante r " +
            "WHERE (:estado IS NULL OR r.localizacao.estado = :estado) " +
            "AND (:cidade IS NULL OR r.localizacao.cidade = :cidade) " +
            "AND (:bairro IS NULL OR r.localizacao.bairro = :bairro) " +
            "AND (:rua IS NULL OR r.localizacao.rua = :rua)")
    List<Restaurante> findByLocalizacao(
            @Param("estado") String estado,
            @Param("cidade") String cidade,
            @Param("bairro") String bairro,
            @Param("rua") String rua);

    List<Restaurante> findByNomeLike(String nome);
}
