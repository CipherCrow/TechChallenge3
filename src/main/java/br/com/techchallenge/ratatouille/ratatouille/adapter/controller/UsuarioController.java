package br.com.techchallenge.ratatouille.ratatouille.adapter.controller;

import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.UsuarioDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.UsuarioMapper;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Usuario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/criar")
    public ResponseEntity<Object> criarUsuario(@Valid @RequestBody UsuarioDTO usuario) {
        try{
            Usuario usuarioCriado = usuarioService.criar(UsuarioMapper.toEntity(usuario));
            return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDTO(usuarioCriado));
        }catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(IdJaExistenteException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Object> buscarUsuario(@PathVariable Long id) {
        try{
            Usuario usuarioEncontrado = usuarioService.buscarPeloId(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(UsuarioMapper.toDTO(usuarioEncontrado));
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Object> atualizarUsuario(@PathVariable Long id,
                                                   @Valid @RequestBody UsuarioDTO usuarioDto) {
        try{
            Usuario usuarioAtualizado = usuarioService.atualizar(id,UsuarioMapper.toEntity(usuarioDto));
            return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toDTO(usuarioAtualizado));
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deletarUsuario(@PathVariable Long id) {
        try{
            Usuario usuarioDeletado = usuarioService.deletarPeloId(id);
            return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toDTO(usuarioDeletado));
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
