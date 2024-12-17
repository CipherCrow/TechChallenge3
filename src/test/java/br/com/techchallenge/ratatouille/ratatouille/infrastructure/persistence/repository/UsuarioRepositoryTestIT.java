package br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.SexoUsuarioEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.UsuarioStatusEnum;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class UsuarioRepositoryTestIT {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void devePermitirGravarUsuario(){
        // Criando marionete
        Usuario usuario = Usuario.builder()
                            .nome("Testonildo da Silva")
                            .email("teste@usuario.com")
                            .idade(22)
                            .sexo(SexoUsuarioEnum.MASCULINO)
                            .status(UsuarioStatusEnum.ATIVO)
                            .build();

        // Realizando Ação
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // Verificando se aconteceu o esperado
        assertThat(usuarioSalvo)
                .isEqualTo(usuario);
    }
}
