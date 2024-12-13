package br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Localizacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.TipoDeCozinhaEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RestauranteRepositoryTest {

    @Mock
    private RestauranteRepository restauranteRepository;

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
    void deveEncontrarPeloTipoDeCozinha() {
        // mockando testes
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setIdRestaurante(1L);
        restaurante1.setNome("Feijoada da Ana");
        restaurante1.setTipoDeCozinha(TipoDeCozinhaEnum.BRASILEIRA);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setIdRestaurante(2L);
        restaurante2.setNome("Bom de Prato");
        restaurante2.setTipoDeCozinha(TipoDeCozinhaEnum.BRASILEIRA);

        List<Restaurante> mockRestaurantes = Arrays.asList(restaurante1, restaurante2);

        when(restauranteRepository.findByTipoDeCozinhaEquals(TipoDeCozinhaEnum.BRASILEIRA)).thenReturn(mockRestaurantes);

        // Act
        List<Restaurante> restaurantes = restauranteRepository.findByTipoDeCozinhaEquals(TipoDeCozinhaEnum.BRASILEIRA);

        // Assert
        assertThat(restaurantes).isNotNull().hasSize(2);
        assertThat(restaurantes.get(0).getIdRestaurante()).isEqualTo(restaurante1.getIdRestaurante());
        assertThat(restaurantes.get(1).getIdRestaurante()).isEqualTo(restaurante2.getIdRestaurante());

        verify(restauranteRepository, times(1)).findByTipoDeCozinhaEquals(TipoDeCozinhaEnum.BRASILEIRA);
    }

    @Test
    void deveEncontrarUmPelaLocalizacao() {
        // Mockando
        Localizacao localizacao1 = new Localizacao();
        localizacao1.setIdLocalizacao(1L);
        localizacao1.setCidade("São Paulo");
        localizacao1.setBairro("Centro");
        localizacao1.setRua("Carvalho de Almeida");

        Localizacao localizacao2 = new Localizacao();
        localizacao1.setIdLocalizacao(1L);
        localizacao1.setCidade("São Paulo");
        localizacao1.setBairro("Centro");
        localizacao1.setRua("Treze");

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setIdRestaurante(1L);
        restaurante1.setNome("Restaurante no Centro");
        restaurante1.setLocalizacao(localizacao1);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setIdRestaurante(2L);
        restaurante2.setNome("Restaurante na Zona Sul");
        restaurante2.setLocalizacao(localizacao2);

        List<Restaurante> mockRestaurantes = List.of(restaurante1);

        when(restauranteRepository.findByLocalizacao(
                null,
                "São Paulo",
                "Centro",
                "Carvalho de Almeida"))
                .thenReturn(mockRestaurantes);

        // Act
        List<Restaurante> restaurantes = restauranteRepository
                .findByLocalizacao(null, "São Paulo", "Centro", "Carvalho de Almeida");

        // Assert
        assertThat(restaurantes).isNotNull()
                .hasSize(1);
        assertThat(restaurantes.get(0).getIdRestaurante()).isEqualTo(restaurante1.getIdRestaurante());
        assertThat(restaurantes).doesNotContain(restaurante2);

        verify(restauranteRepository, times(1))
                .findByLocalizacao(null, "São Paulo", "Centro", "Carvalho de Almeida");
    }

    @Test
    void deveEncontrarTodosPelaLocalizacao() {
        // Mockando
        Localizacao localizacao1 = new Localizacao();
        localizacao1.setIdLocalizacao(1L);
        localizacao1.setCidade("São Paulo");
        localizacao1.setRua("Carvalho de Almeida");

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setIdRestaurante(1L);
        restaurante1.setNome("Restaurante no Centro");
        restaurante1.setLocalizacao(localizacao1);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setIdRestaurante(2L);
        restaurante2.setNome("Restaurante na Zona Sul");
        restaurante2.setLocalizacao(localizacao1);

        List<Restaurante> mockRestaurantes = List.of(restaurante1,restaurante2);

        when(restauranteRepository.findByLocalizacao(
                null,
                "São Paulo",
                null,
                "Carvalho de Almeida"))
                .thenReturn(mockRestaurantes);

        // Act
        List<Restaurante> restaurantes = restauranteRepository
                .findByLocalizacao(null, "São Paulo", null, "Carvalho de Almeida");

        // Assert
        assertThat(restaurantes).isNotNull()
                .hasSize(2);
        assertThat(restaurantes.get(0).getIdRestaurante()).isEqualTo(restaurante1.getIdRestaurante());
        assertThat(restaurantes.get(1).getIdRestaurante()).isEqualTo(restaurante2.getIdRestaurante());

        verify(restauranteRepository, times(1))
                .findByLocalizacao(null, "São Paulo", null, "Carvalho de Almeida");
    }

    @Test
    void deveEncontrarTodosPeloNome() {
        // mockando testes
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setIdRestaurante(1L);
        restaurante1.setNome("Pestiscos Voli");

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setIdRestaurante(2L);
        restaurante2.setNome("Pestisco do Juca");

        List<Restaurante> mockRestaurantes = Arrays.asList(restaurante1, restaurante2);

        when(restauranteRepository.findByNomeLike("Petisco"))
                .thenReturn(mockRestaurantes);

        // Act
        List<Restaurante> restaurantes = restauranteRepository.findByNomeLike("Petisco");

        // Assert
        assertThat(restaurantes).isNotNull();
        assertThat(restaurantes).hasSize(2);
        assertThat(restaurantes.get(0).getIdRestaurante()).isEqualTo(restaurante1.getIdRestaurante());
        assertThat(restaurantes.get(1).getIdRestaurante()).isEqualTo(restaurante2.getIdRestaurante());

        verify(restauranteRepository, times(1)).findByNomeLike("Petisco");
    }
}
