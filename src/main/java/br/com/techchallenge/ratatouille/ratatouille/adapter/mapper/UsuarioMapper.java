package br.com.techchallenge.ratatouille.ratatouille.adapter.mapper;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.UsuarioDTO;

public class UsuarioMapper {

    private UsuarioMapper(){
        throw new IllegalStateException("Classe de utilidade");
    }

    public static UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getIdade(),
                usuario.getSexo(),
                usuario.getStatus()
        );
    }

    public static Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(dto.idUsuario());
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setIdade(dto.idade());
        usuario.setSexo(dto.sexo());
        usuario.setStatus(dto.status());

        return usuario;
    }
}