package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegraDeNegocioException;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.UsuarioStatusEnum;
import br.com.techchallenge.ratatouille.ratatouille.infrastructure.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private static String idNotNull = "ID não pode ser nulo";

    private final UsuarioRepository usuarioRepository;

    public Usuario criar(Usuario usuarioParametro) {
        //usuarioParametro.setIdUsuario(null);
        usuarioParametro.setStatus(UsuarioStatusEnum.ATIVO);
         return usuarioRepository.save(usuarioParametro);
    }

    public Usuario buscarPeloId(Long idUsuario) {
        Objects.requireNonNull(idUsuario, idNotNull);

        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegistroNotFoundException("Usuário",idUsuario));
    }

    public Usuario atualizar(Long idUsuario,Usuario usuarioParametro) {
        Objects.requireNonNull(idUsuario, idNotNull);

        Usuario usuario = this.buscarPeloId(idUsuario);
        if (usuarioParametro.getNome() != null){
            usuario.setNome(usuarioParametro.getNome());
        }
        if (usuarioParametro.getEmail() != null){
            usuario.setEmail(usuarioParametro.getEmail());
        }
        if(usuarioParametro.getIdade() != null ){
            usuario.setIdade(usuarioParametro.getIdade());
        }
        if(usuarioParametro.getSexo() != null ){
            usuario.setSexo(usuarioParametro.getSexo());
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

        if(usuario.getStatus() == UsuarioStatusEnum.INATIVO){
            throw new RegraDeNegocioException("Usuário já está inativado!");
        }

        usuario.setStatus(UsuarioStatusEnum.INATIVO);
        return usuarioRepository.save(usuario);
    }
}
