package br.com.techchallenge.ratatouille.domain.model.service;


import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.LocalizacaoService;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.LocalizacaoServiceImpl;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.LocalizacaoRepository;
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

class LocalizacaoServiceTest {

    private LocalizacaoService localizacaoService;

    @Mock
    private LocalizacaoRepository localizacaoRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
        this.localizacaoService = new LocalizacaoServiceImpl(localizacaoRepository);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }

    @Nested
    class CriarLocalizacao{
        @Test
        void deveConseguirCriarLocalizacaoComSucesso() {
            // Arrange
            Localizacao localizacao = new Localizacao();
            localizacao.setIdLocalizacao(1L);
            localizacao.setEstado("SP");
            localizacao.setCidade("São Paulo");
            localizacao.setBairro("Centro");
            localizacao.setRua("Rua A");
            localizacao.setNumero("123");

            when(localizacaoRepository.existsById(1L)).thenReturn(false);
            when(localizacaoRepository.save(any(Localizacao.class))).thenReturn(localizacao);

            // Act
            Localizacao localizacaoCriada = localizacaoService.criar(localizacao);

            // Assert
            assertThat(localizacaoCriada).isNotNull();
            assertThat(localizacaoCriada.getIdLocalizacao()).isEqualTo(1L);
            assertThat(localizacaoCriada.getCidade()).isEqualTo("São Paulo");
            verify(localizacaoRepository, times(1)).existsById(1L);
            verify(localizacaoRepository, times(1)).save(localizacao);
        }

        @Test
        void deveLancarExcecaoAoCriarLocalizacaoComIdExistente() {
            // Arrange
            Localizacao localizacao = new Localizacao();
            localizacao.setIdLocalizacao(1L);

            when(localizacaoRepository.existsById(1L)).thenReturn(true);

            // Assert
            assertThatThrownBy(() -> localizacaoService.criar(localizacao))
                    .isInstanceOf(IdJaExistenteException.class)
                    .hasMessage("Id da localizacao já existente!");
        }
    }

    @Nested
    class BuscarLocalizacao{
        @Test
        void deveConseguirBuscarLocalizacaoPeloId() {
            // Arrange
            Localizacao localizacao = new Localizacao();
            localizacao.setIdLocalizacao(1L);
            localizacao.setCidade("São Paulo");

            when(localizacaoRepository.findById(1L)).thenReturn(Optional.of(localizacao));

            // Act
            Localizacao localizacaoEncontrada = localizacaoService.buscarPeloId(1L);

            // Assert
            assertThat(localizacaoEncontrada).isNotNull();
            assertThat(localizacaoEncontrada.getIdLocalizacao()).isEqualTo(1L);
            assertThat(localizacaoEncontrada.getCidade()).isEqualTo("São Paulo");
            verify(localizacaoRepository, times(1)).findById(1L);
        }

        @Test
        void deveLancarExcecaoAoBuscarLocalizacaoInexistente() {
            // Arrange
            Long idLocalizacao = 1L;

            when(localizacaoRepository.findById(idLocalizacao)).thenReturn(Optional.empty());

            // Assert
            assertThatThrownBy(() -> localizacaoService.buscarPeloId(idLocalizacao))
                    .isInstanceOf(RegistroNotFoundException.class)
                    .hasMessageContaining("Localização");
        }
    }

    @Nested
    class AtualizarLocalizacao{
        @Test
        void deveConseguirAtualizarLocalizacao() {
            // Arrange
            Localizacao localizacaoExistente = new Localizacao();
            localizacaoExistente.setIdLocalizacao(1L);
            localizacaoExistente.setRua("Rua A");

            Localizacao localizacaoAtualizada = new Localizacao();
            localizacaoAtualizada.setIdLocalizacao(1L);
            localizacaoAtualizada.setRua("Rua B");

            when(localizacaoRepository.findById(1L)).thenReturn(Optional.of(localizacaoExistente));
            when(localizacaoRepository.save(any(Localizacao.class))).thenReturn(localizacaoExistente);

            // Act
            Localizacao localizacao = localizacaoService.atualizar(localizacaoAtualizada);

            // Assert
            assertThat(localizacao).isNotNull();
            assertThat(localizacao.getRua()).isEqualTo("Rua B");
            verify(localizacaoRepository, times(1)).findById(1L);
            verify(localizacaoRepository, times(1)).save(localizacaoExistente);
        }

        @Test
        void deveLancarExcecaoAoAtualizarSemId() {
            // Arrange
            Localizacao localizacaoSemId = new Localizacao();

            // Act & Assert
            assertThatThrownBy(() -> localizacaoService.atualizar(localizacaoSemId))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("ID não pode ser nulo");
        }
    }
    }
