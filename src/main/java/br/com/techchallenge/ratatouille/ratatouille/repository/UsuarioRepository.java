package br.com.techchallenge.ratatouille.ratatouille.repository;

import br.com.techchallenge.ratatouille.ratatouille.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
