package br.com.techchallenge.ratatouille.ratatouille.service;

import br.com.techchallenge.ratatouille.ratatouille.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto.UsuarioDTO;
import br.com.techchallenge.ratatouille.ratatouille.model.enums.UsuarioStatusEnum;
import br.com.techchallenge.ratatouille.ratatouille.repository.UsuarioRepository;
import br.com.techchallenge.ratatouille.ratatouille.service.mapper.UsuarioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
    private static String idNotNull = "ID não pode ser nulo";

    // Sonar Lint Detectou perigo de injeção
    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario criarUsuario(UsuarioDTO usuarioDTO) {
        log.info("Criando usuário ID: {}",usuarioDTO.idUsuario());
        Long parametroID = usuarioDTO.idUsuario();

        if (usuarioRepository.existsById(parametroID)) {
            log.info("ID ja existe. ID: {}", parametroID);
            throw new IllegalArgumentException("ID já existe");
        }

        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarUsuario(Long idUsuario) {
        Objects.requireNonNull(idUsuario, idNotNull);

        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegistroNotFoundException("Usuário",idUsuario));
    }

    public Usuario atualizarUsuario(UsuarioDTO usuarioDTO) {
        Objects.requireNonNull(usuarioDTO.idUsuario(), idNotNull);

        Usuario usuario = this.buscarUsuario(usuarioDTO.idUsuario());
        usuario.setNome(usuarioDTO.nome());
        usuario.setEmail(usuarioDTO.email());
        usuario.setIdade(usuarioDTO.idade());
        usuario.setSexo(usuarioDTO.sexo());

        return usuarioRepository.save(usuario);
    }

    /***
     *
     * Definido pela regra de negocio que não deletaremos usuários da base para manter o registro. O delete lógico acontecerá pelo status
     *
     * ***/
    public Usuario deletarUsuario(Long idUsuario) {
        Objects.requireNonNull(idUsuario, idNotNull);

        Usuario usuario = this.buscarUsuario(idUsuario);
        usuario.setStatus(UsuarioStatusEnum.INATIVO);
        return usuarioRepository.save(usuario);
    }
}
