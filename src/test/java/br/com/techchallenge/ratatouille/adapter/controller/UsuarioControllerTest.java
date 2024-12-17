package br.com.techchallenge.ratatouille.adapter.controller;

import br.com.techchallenge.ratatouille.ratatouille.adapter.controller.UsuarioController;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.UsuarioDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.UsuarioMapper;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.SexoUsuarioEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.UsuarioStatusEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
    }

    @Nested
    class CriarUsuario {

        @Test
        void deveCriarUsuarioComSucesso() throws Exception {
            UsuarioDTO usuarioDTO = new UsuarioDTO(null,"João", "joao@example.com", 25, SexoUsuarioEnum.MASCULINO, UsuarioStatusEnum.ATIVO);
            Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
            usuario.setIdUsuario(1L);

            when(usuarioService.criar(any(Usuario.class))).thenReturn(usuario);


            mockMvc.perform(post("/usuario/criar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(usuarioDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idUsuario").value(1L))
                    .andExpect(jsonPath("$.nome").value("João"))
                    .andExpect(jsonPath("$.email").value("joao@example.com"));

            verify(usuarioService, times(1)).criar(any(Usuario.class));
        }

        @Test
        void deveRetornarErroParaUsuarioInvalido() throws Exception {
            UsuarioDTO usuarioDTO = new UsuarioDTO(4L, "", null, null,null,null);

            mockMvc.perform(post("/usuario/criar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(usuarioDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class BuscarUsuario {

        @Test
        void deveRetornarUsuarioExistente() throws Exception {
            Usuario usuario = new Usuario(1L, "Maria", "maria@example.com", 30, SexoUsuarioEnum.FEMININO, UsuarioStatusEnum.ATIVO);

            when(usuarioService.buscarPeloId(1L)).thenReturn(usuario);

            mockMvc.perform(get("/usuario/buscar/1"))
                    .andExpect(status().isFound())
                    .andExpect(jsonPath("$.idUsuario").value(1L))
                    .andExpect(jsonPath("$.nome").value("Maria"))
                    .andExpect(jsonPath("$.email").value("maria@example.com"));

            verify(usuarioService, times(1)).buscarPeloId(1L);
        }

        @Test
        void deveRetornarErroParaUsuarioInexistente() throws Exception {
            when(usuarioService.buscarPeloId(1L)).thenThrow(new RegistroNotFoundException("Usuário", 1L));

            mockMvc.perform(get("/usuario/buscar/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Usuário não encontrado com ID: 1"));

            verify(usuarioService, times(1)).buscarPeloId(1L);
        }
    }

    @Nested
    class AtualizarUsuario {

        @Test
        @DisplayName("Deve atualizar um usuário com sucesso")
        void deveAtualizarUsuarioComSucesso() throws Exception {
            UsuarioDTO usuarioDTO = new UsuarioDTO(1L,"Carlos", "carlos@example.com", 40, SexoUsuarioEnum.MASCULINO,UsuarioStatusEnum.ATIVO);
            Usuario usuarioAtualizado = UsuarioMapper.toEntity(usuarioDTO);
            usuarioAtualizado.setIdUsuario(1L);

            when(usuarioService.atualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

            mockMvc.perform(put("/usuario/atualizar/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(usuarioDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idUsuario").value(1L))
                    .andExpect(jsonPath("$.nome").value("Carlos"))
                    .andExpect(jsonPath("$.email").value("carlos@example.com"));

            verify(usuarioService, times(1)).atualizar(eq(1L), any(Usuario.class));
        }
    }

    @Nested
    class DeletarUsuario {

        @Test
        void deveDeletarUsuarioComSucesso() throws Exception {
            Usuario usuario = new Usuario(1L, "Joana", "joana@example.com", 28, SexoUsuarioEnum.FEMININO, UsuarioStatusEnum.INATIVO);

            when(usuarioService.deletarPeloId(1L)).thenReturn(usuario);

            mockMvc.perform(delete("/usuario/delete/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idUsuario").value(1L))
                    .andExpect(jsonPath("$.nome").value("Joana"));

            verify(usuarioService, times(1)).deletarPeloId(1L);
        }
    }

    // Método utilitário para converter objetos em JSON
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
