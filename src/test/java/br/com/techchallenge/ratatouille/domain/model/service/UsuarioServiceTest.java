package br.com.techchallenge.ratatouille.domain.model.service;


import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegraDeNegocioException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.SexoUsuarioEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.UsuarioStatusEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.UsuarioService;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.UsuarioServiceImpl;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
        this.usuarioService = new UsuarioServiceImpl(usuarioRepository);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }

    @Nested
    class criarUsuario{
        @Test
        void deveConseguirCriarUsuarioComSucesso() {
            // Arrange
            Usuario usuario =
                    new Usuario(1L, "João", "joao@email.com", 30, SexoUsuarioEnum.MASCULINO,null);

            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(
                    i -> i.getArgument(0));

            // Act
            Usuario usuarioCriado = usuarioService.criar(usuario);

            // Assert
            assertThat(usuarioCriado).isNotNull();
            assertThat(usuarioCriado.getIdUsuario()).isEqualTo(1L);
            assertThat(usuarioCriado.getNome()).isEqualTo("João");
            verify(usuarioRepository, times(1)).save(usuario);

        }
    }

    @Nested
    class buscarUsuario{
        @Test
        void deveLancarExcecaoAoBuscarUsuarioInexistente() {
            // Arrange
            Long idUsuario = 1L;
            when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

            // Act
            assertThatThrownBy(() -> usuarioService.buscarPeloId(idUsuario))
                    .isInstanceOf(RegistroNotFoundException.class)
                    .hasMessageContaining("Usuário");
        }

        @Test
        void deveConseguirBuscarUsuarioPeloId() {
            // Arrange
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(1L);
            usuario.setNome("João");
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            // Act
            Usuario usuarioEncontrado = usuarioService.buscarPeloId(1L);

            // Assert
            assertThat(usuarioEncontrado).isNotNull();
            assertThat(usuarioEncontrado.getIdUsuario()).isEqualTo(1L);
            assertThat(usuarioEncontrado.getNome()).isEqualTo("João");
            verify(usuarioRepository, times(1)).findById(1L);
        }
    }

    @Nested
    class atualizarUsuario {
        @Test
        void deveConseguirAtualizarUsuario() {
            // Arrange
            Usuario usuarioExistente = new Usuario();
            usuarioExistente.setIdUsuario(1L);
            usuarioExistente.setNome("João");

            Usuario usuarioAtualizado = new Usuario();
            usuarioAtualizado.setNome("João Atualizado");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

            // Act
            Usuario usuario = usuarioService.atualizar(1L, usuarioAtualizado);

            // Assert
            assertThat(usuario).isNotNull();
            assertThat(usuario.getNome()).isEqualTo("João Atualizado");
            verify(usuarioRepository, times(1)).save(usuarioExistente);
        }
    }

    @Nested
    class removerUsuario {
        @Test
        void deveConseguirInativarUsuario() {
            // Arrange
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(1L);
            usuario.setStatus(UsuarioStatusEnum.ATIVO);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

            // Act
            Usuario usuarioInativado = usuarioService.deletarPeloId(1L);

            // Assert
            assertThat(usuarioInativado).isNotNull();
            assertThat(usuarioInativado.getStatus()).isEqualTo(UsuarioStatusEnum.INATIVO);
            verify(usuarioRepository, times(1)).save(usuario);
        }

        @Test
        void deveLancarExcecaoAoInativarUsuarioJaInativo() {
            // Arrange
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(1L);
            usuario.setStatus(UsuarioStatusEnum.INATIVO);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

            // Act
            assertThatThrownBy(() -> usuarioService.deletarPeloId(1L))
                    .isInstanceOf(RegraDeNegocioException.class)
                    .hasMessage("Usuário já está inativado!");
        }
    }
}
