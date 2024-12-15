package br.com.techchallenge.ratatouille.domain.model.service;


import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.TipoDeCozinhaEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.*;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.RestauranteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestauranteServiceTest {

    private RestauranteServiceImpl restauranteService;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private LocalizacaoService localizacaoService;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
        this.restauranteService = new RestauranteServiceImpl( restauranteRepository, localizacaoService);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }

    @Nested
    class CriarRestaurante{

        @Test
        void deveCriarRestauranteComSucesso() {
            // Arrange
            Restaurante restaurante = new Restaurante();
            restaurante.setIdRestaurante(1L);
            restaurante.setNome("Restaurante Teste");

            when(restauranteRepository.existsById(1L)).thenReturn(false);
            when(restauranteRepository.save(restaurante)).thenReturn(restaurante);

            // Act
            Restaurante resultado = restauranteService.criar(restaurante);

            // Assert
            assertNotNull(resultado);
            assertEquals(1L, resultado.getIdRestaurante());
            verify(restauranteRepository).existsById(1L);
            verify(restauranteRepository).save(restaurante);
        }

        @Test
        void deveLancarExcecao_QuandoIdJaExistir() {
            // Arrange
            Restaurante restaurante = new Restaurante();
            restaurante.setIdRestaurante(1L);

            when(restauranteRepository.existsById(1L)).thenReturn(true);

            // Act
            assertThrows(IdJaExistenteException.class, () -> restauranteService.criar(restaurante));
            verify(restauranteRepository).existsById(1L);
            verify(restauranteRepository, never()).save(any());
        }
    }

    @Nested
    class BuscarRestaurante {

        @Test
        void deveRetornarRestaurantePeloId() {
            // Arrange
            Restaurante restaurante = new Restaurante();
            restaurante.setIdRestaurante(1L);
            restaurante.setNome("Restaurante Teste");

            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

            // Act
            Restaurante resultado = restauranteService.buscarPeloId(1L);

            // Assert
            assertNotNull(resultado);
            assertEquals("Restaurante Teste", resultado.getNome());
            verify(restauranteRepository).findById(1L);
        }

        @Test
        void deveLancarExcecao_QuandoNaoEncontrarRestaurante() {
            // Arrange
            when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

            // Act
            assertThrows(RegistroNotFoundException.class, () -> restauranteService.buscarPeloId(1L));
            verify(restauranteRepository).findById(1L);
        }
    }

    @Nested
    class AtualizarDadosRestaurante{

        @Test
        void deveAtualizarDadosComSucesso() {
            // Arrange
            Restaurante restaurante = new Restaurante();
            restaurante.setIdRestaurante(1L);
            restaurante.setNome("Antigo Nome");
            restaurante.setTipoDeCozinha(TipoDeCozinhaEnum.BRASILEIRA);

            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(restauranteRepository.save(any(Restaurante.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Restaurante resultado = restauranteService.atualizarDados(1L, "Novo Nome", TipoDeCozinhaEnum.JAPONESA);

            // Assert
            assertNotNull(resultado);
            assertEquals("Novo Nome", resultado.getNome());
            assertEquals(TipoDeCozinhaEnum.JAPONESA, resultado.getTipoDeCozinha());
            verify(restauranteRepository).findById(1L);
            verify(restauranteRepository).save(any(Restaurante.class));
        }

        @Test
        void deveLancarExcecao_SeDadosForemNulos() {
            // Arrange
            when(restauranteRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act
            assertThrows(NullPointerException.class, () -> restauranteService.atualizarDados(null, null, null));
        }
    }

    @Nested
    class AtualizarLocalizacao{

        @Test
        void deveAtualizarLocalizacaoComSucesso() {
            // Arrange
            Long idRestaurante = 1L;
            Restaurante restaurante = new Restaurante();
            restaurante.setIdRestaurante(idRestaurante);
            Localizacao localizacao = new Localizacao();
            localizacao.setCidade("Nova Cidade");

            when(restauranteRepository.findById(idRestaurante)).thenReturn(Optional.of(restaurante));
            when(localizacaoService.atualizar(any(Localizacao.class))).thenReturn(localizacao);
            //doNothing().when(localizacaoService).atualizar(localizacao);

            // Act
            Restaurante resultado = restauranteService.atualizarLocalizacao(idRestaurante, localizacao);

            // Assert
            assertNotNull(resultado);
            verify(localizacaoService).atualizar(localizacao);
            verify(restauranteRepository).findById(idRestaurante);
        }

        @Test
        void deveLancarExcecao_SeLocalizacaoForNula() {
            // Arrange
            Long idRestaurante = 1L;
            when(restauranteRepository.findById(idRestaurante)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NullPointerException.class, () -> restauranteService.atualizarLocalizacao(idRestaurante, null));
        }
    }

    @Nested
    class BuscarRestaurantes {

        @Test
        void deveBuscarPorNome() {
            // Arrange
            String nome = "Restaurante Teste";
            List<Restaurante> restaurantes = List.of(new Restaurante());

            when(restauranteRepository.findByNomeLike(nome)).thenReturn(restaurantes);

            // Act
            List<Restaurante> resultado = restauranteService.buscarPeloNome(nome);

            // Assert
            assertNotNull(resultado);
            assertFalse(resultado.isEmpty());
            verify(restauranteRepository).findByNomeLike(nome);
        }

        @Test
        void deveBuscarPorLocalizacao() {
            // Arrange
            Localizacao localizacao = new Localizacao();
            localizacao.setEstado("Estado Teste");
            localizacao.setCidade("Cidade Teste");
            localizacao.setBairro("Bairro Teste");
            localizacao.setRua("Rua Teste");

            List<Restaurante> restaurantes = List.of(new Restaurante());

            when(restauranteRepository.findByLocalizacao(
                    localizacao.getEstado(),
                    localizacao.getCidade(),
                    localizacao.getBairro(),
                    localizacao.getRua()))
                    .thenReturn(restaurantes);

            // Act
            List<Restaurante> resultado = restauranteService.buscarPelaLocalizacao(localizacao);

            // Assert
            assertNotNull(resultado);
            assertFalse(resultado.isEmpty());
            verify(restauranteRepository).findByLocalizacao(
                    localizacao.getEstado(),
                    localizacao.getCidade(),
                    localizacao.getBairro(),
                    localizacao.getRua());
        }
    }

}
