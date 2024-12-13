package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.UsuarioDTO;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.UsuarioStatusEnum;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.UsuarioRepository;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsuarioService {

    private static String idNotNull = "ID não pode ser nulo";

    // Sonar Lint Detectou perigo de injeção
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario criar(UsuarioDTO usuarioDTO) {
        Long parametroID = usuarioDTO.idUsuario();

        if (usuarioRepository.existsById(parametroID)) {
            throw new IdJaExistenteException("Id do usuario já existente!");
        }

        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPeloId(Long idUsuario) {
        Objects.requireNonNull(idUsuario, idNotNull);

        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegistroNotFoundException("Usuário",idUsuario));
    }

    public Usuario atualizar(Long idUsuario,UsuarioDTO usuarioDTO) {
        Objects.requireNonNull(idUsuario, idNotNull);

        Usuario usuario = this.buscarPeloId(idUsuario);
        if (usuarioDTO.nome() != null){
            usuario.setNome(usuarioDTO.nome());
        }
        if (usuarioDTO.email() != null){
            usuario.setEmail(usuarioDTO.email());
        }
        if(usuarioDTO.idade() != null ){
            usuario.setIdade(usuarioDTO.idade());
        }
        if(usuarioDTO.sexo() != null ){
            usuario.setSexo(usuarioDTO.sexo());
        }
        return usuarioRepository.save(usuario);
    }

    /***
     *
     * Definido pela regra de negocio que não deletaremos usuários da base para manter o registro. O delete lógico acontecerá pelo status
     *
     * ***/
    public Usuario deletarPeloId(Long idUsuario) {
        Objects.requireNonNull(idUsuario, idNotNull);

        Usuario usuario = this.buscarPeloId(idUsuario);
        usuario.setStatus(UsuarioStatusEnum.INATIVO);
        return usuarioRepository.save(usuario);
    }
}
