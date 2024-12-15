package br.com.techchallenge.ratatouille.adapter.controller;

import br.com.techchallenge.ratatouille.ratatouille.adapter.controller.AvaliacaoController;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.AvaliacaoDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.AvaliacaoMapper;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Avaliacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.AvaliacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class AvaliacaoControllerTest {

    @Mock
    private AvaliacaoService avaliacaoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        AvaliacaoController avaliacaoController = new AvaliacaoController(avaliacaoService);
        mockMvc = MockMvcBuilders.standaloneSetup(avaliacaoController).build();
    }

    @Nested
    class criarAvaliacao {
        @Test
        void deveCriarAvaliacaoComSucesso() throws Exception {
            // Dados
            AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO(null, new Restaurante(),"Excelente comida!", 5,new Usuario());
            Avaliacao avaliacao = AvaliacaoMapper.toEntity(avaliacaoDTO);
            avaliacao.setIdAvaliacao(1L);

            // Mock
            when(avaliacaoService.criar(eq(1L), eq(1L), any(Avaliacao.class))).thenReturn(avaliacao);

            // Execução
            mockMvc.perform(post("/avaliacao/avaliar")
                            .param("idRestaurante", "1")
                            .param("idUsuario", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(avaliacaoDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idAvaliacao").value(1L))
                    .andExpect(jsonPath("$.comentario").value("Excelente comida!"))
                    .andExpect(jsonPath("$.estrelas").value(5));

            // Verificação
            verify(avaliacaoService, times(1)).criar(eq(1L), eq(1L), any(Avaliacao.class));
        }
    }

    @Nested
    class buscarAvaliacao {
        @Test
        void deveBuscarAvaliacaoPorIdComSucesso() throws Exception {
            // Mock
            Avaliacao avaliacao = new Avaliacao(1L, new Restaurante(),"Excelente comida!", 4,new Usuario());
            when(avaliacaoService.buscarPeloId(1L)).thenReturn(avaliacao);

            //  teste
            mockMvc.perform(get("/avaliacao/buscar/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isFound())
                    .andExpect(jsonPath("$.idAvaliacao").value(1L))
                    .andExpect(jsonPath("$.comentario").value("Excelente comida!"))
                    .andExpect(jsonPath("$.estrelas").value(4));

            // Verificação
            verify(avaliacaoService, times(1)).buscarPeloId(1L);
        }

        @Test
        void deveBuscarAvaliacoesDeRestauranteComSucesso() throws Exception {
            // Mock
            List<Avaliacao> avaliacoes = List.of(
                    new Avaliacao(1L, new Restaurante(),"Excelente comida!", 5,new Usuario()),
                    new Avaliacao(2L, new Restaurante(),"Achei Morno!", 4,new Usuario())
            );
            when(avaliacaoService.buscarTodasAvaliacoesRestaurante(1L)).thenReturn(avaliacoes);

            // teste
            mockMvc.perform(get("/avaliacao/buscarAvaliacoesDoRestaurante/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isFound())
                    .andExpect(jsonPath("$[0].idAvaliacao").value(1L))
                    .andExpect(jsonPath("$[0].comentario").value("Excelente comida!"))
                    .andExpect(jsonPath("$[0].estrelas").value(5))
                    .andExpect(jsonPath("$[1].idAvaliacao").value(2L))
                    .andExpect(jsonPath("$[1].comentario").value("Achei Morno!"))
                    .andExpect(jsonPath("$[1].estrelas").value(4));

            // Verificação
            verify(avaliacaoService, times(1)).buscarTodasAvaliacoesRestaurante(1L);
        }
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
