package br.com.techchallenge.ratatouille.domain.model.service;


import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegraDeNegocioException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Avaliacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.*;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.AvaliacaoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AvaliacaoServiceTest {

    @InjectMocks
    private AvaliacaoServiceImpl avaliacaoService;

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @Mock
    private RestauranteService restauranteService;

    @Mock
    private UsuarioService usuarioService;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
        this.avaliacaoService = new AvaliacaoServiceImpl(avaliacaoRepository, restauranteService, usuarioService);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }

    @Nested
    class criarAvaliacao{
        @Test
        void deveCriarAvaliacaoComSucesso() {
            // Arrange
            Long idRestaurante = 1L;
            Long idUsuario = 2L;

            Restaurante restaurante = new Restaurante();
            restaurante.setIdRestaurante(idRestaurante);

            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuario);

            Avaliacao avaliacaoParam = new Avaliacao();
            avaliacaoParam.setEstrelas(5);

            when(restauranteService.buscarPeloId(idRestaurante)).thenReturn(restaurante);
            when(usuarioService.buscarPeloId(idUsuario)).thenReturn(usuario);
            when(avaliacaoRepository.save(any(Avaliacao.class))).thenReturn(avaliacaoParam);

            // Act
            Avaliacao avaliacaoCriada = avaliacaoService.criar(idRestaurante, idUsuario, avaliacaoParam);

            // Assert
            assertThat(avaliacaoCriada).isNotNull();
            assertThat(avaliacaoCriada.getEstrelas()).isEqualTo(5);
            verify(restauranteService).buscarPeloId(idRestaurante);
            verify(usuarioService).buscarPeloId(idUsuario);
            verify(avaliacaoRepository).save(any(Avaliacao.class));
        }

        @Test
        void deveLancarExcecaoAoCriarAvaliacaoComIdExistente() {
            // Arrange
            Avaliacao avaliacaoParam = new Avaliacao();
            avaliacaoParam.setIdAvaliacao(1L);

            when(avaliacaoRepository.existsById(1L)).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> avaliacaoService.criar(1L, 2L, avaliacaoParam))
                    .isInstanceOf(IdJaExistenteException.class)
                    .hasMessage("Id da avaliacao já existente!");
        }

        @Test
        void deveLancarExcecaoAoCriarAvaliacaoComEstrelasInvalidas() {
            // Arrange
            Avaliacao avaliacaoParam = new Avaliacao();
            avaliacaoParam.setEstrelas(6);

            // Act & Assert
            assertThatThrownBy(() -> avaliacaoService.criar(1L, 2L, avaliacaoParam))
                    .isInstanceOf(RegraDeNegocioException.class)
                    .hasMessage("Deve ser dado de 1 à 5 estrelas de avaliação!");
        }
    }

    @Nested
    class buscarAvaliacao{
        @Test
        void deveConseguirBuscarAvaliacaoPeloId() {
            // Arrange
            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setIdAvaliacao(1L);
            avaliacao.setEstrelas(5);

            when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));

            // Act
            Avaliacao avaliacaoEncontrada = avaliacaoService.buscarPeloId(1L);

            // Assert
            assertThat(avaliacaoEncontrada).isNotNull();
            assertThat(avaliacaoEncontrada.getIdAvaliacao()).isEqualTo(1L);
            assertThat(avaliacaoEncontrada.getEstrelas()).isEqualTo(5);
            verify(avaliacaoRepository, times(1)).findById(1L);
        }

        @Test
        void deveLancarExcecaoAoBuscarAvaliacaoInexistente() {
            // Arrange
            when(avaliacaoRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> avaliacaoService.buscarPeloId(1L))
                    .isInstanceOf(RegistroNotFoundException.class)
                    .hasMessageContaining("Avaliacao");
        }

        @Test
        void deveBuscarTodasAvaliacoesDoRestaurante() {
            // Arrange
            Long idRestaurante = 1L;
            Restaurante restaurante = new Restaurante();
            restaurante.setIdRestaurante(idRestaurante);

            Avaliacao avaliacao1 = new Avaliacao();
            avaliacao1.setIdAvaliacao(1L);
            avaliacao1.setEstrelas(4);

            Avaliacao avaliacao2 = new Avaliacao();
            avaliacao2.setIdAvaliacao(2L);
            avaliacao2.setEstrelas(5);

            when(restauranteService.buscarPeloId(idRestaurante)).thenReturn(restaurante);
            when(avaliacaoRepository.findAvaliacaosByRestaurante(restaurante)).thenReturn(List.of(avaliacao1, avaliacao2));

            // Act
            List<Avaliacao> avaliacoes = avaliacaoService.buscarTodasAvaliacoesRestaurante(idRestaurante);

            // Assert
            assertThat(avaliacoes).hasSize(2);
            assertThat(avaliacoes).extracting(Avaliacao::getIdAvaliacao).containsExactlyInAnyOrder(1L, 2L);
            verify(restauranteService, times(1)).buscarPeloId(idRestaurante);
            verify(avaliacaoRepository, times(1)).findAvaliacaosByRestaurante(restaurante);
        }
    }
}
