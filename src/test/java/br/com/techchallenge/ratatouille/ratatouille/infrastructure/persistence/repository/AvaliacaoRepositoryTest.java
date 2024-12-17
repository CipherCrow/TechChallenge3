package br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Avaliacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AvaliacaoRepositoryTest {

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }

    @Test
    void deveEncontrarAvaliacoesPeloRestaurante() {
        // Mokando testes
        Restaurante restauranteMock = new Restaurante();
        restauranteMock.setIdRestaurante(1L);
        restauranteMock.setNome("Restaurante Teste");

        Usuario usuarioMock = new Usuario();
        usuarioMock.setIdUsuario(1L);
        usuarioMock.setNome("Paulinho");

        Avaliacao avaliacao1 = new Avaliacao();
        avaliacao1.setIdAvaliacao(1L);
        avaliacao1.setComentario("Excelente comida!");
        avaliacao1.setEstrelas(5);
        avaliacao1.setRestaurante(restauranteMock);
        avaliacao1.setUsuario(usuarioMock);

        Avaliacao avaliacao2 = new Avaliacao();
        avaliacao2.setIdAvaliacao(2L);
        avaliacao2.setComentario("Bom atendimento.");
        avaliacao2.setEstrelas(4);
        avaliacao2.setRestaurante(restauranteMock);
        avaliacao2.setUsuario(usuarioMock);

        List<Avaliacao> mockAvaliacoes = Arrays.asList(avaliacao1, avaliacao2);

        when(avaliacaoRepository.findAvaliacaosByRestaurante(restauranteMock)).thenReturn(mockAvaliacoes);

        // Act
        List<Avaliacao> avaliacoes = avaliacaoRepository.findAvaliacaosByRestaurante(restauranteMock);

        // Assert
        assertThat(avaliacoes).isNotNull()
                .hasSize(2);
        assertThat(avaliacoes).contains(avaliacao1, avaliacao2);
        assertThat(avaliacoes.get(0).getIdAvaliacao()).isEqualTo(avaliacao1.getIdAvaliacao());
        assertThat(avaliacoes.get(1).getIdAvaliacao()).isEqualTo(avaliacao2.getIdAvaliacao());

        verify(avaliacaoRepository, times(1)).findAvaliacaosByRestaurante(restauranteMock);
    }
}
