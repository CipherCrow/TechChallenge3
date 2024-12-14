package br.com.techchallenge.ratatouille.ratatouille.domain.model.service;

import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;

public interface UsuarioService {

    public Usuario criar(Usuario usuarioParametro);

    public Usuario buscarPeloId(Long idUsuario) ;

    public Usuario atualizar(Long idUsuario,Usuario usuario) ;

    /***
     *
     * Definido pela regra de negocio que não deletaremos usuários da base para manter o registro. O delete lógico acontecerá pelo status
     *
     * ***/
    public Usuario deletarPeloId(Long idUsuario) ;
}
