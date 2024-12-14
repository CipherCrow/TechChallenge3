package br.com.techchallenge.ratatouille.ratatouille.adapter.controller;

import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.AvaliacaoDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegraDeNegocioException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.AvaliacaoMapper;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Avaliacao;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/avaliacao")
public class AvaliacaoController {

    @Autowired
    AvaliacaoService avaliacaoService;

    @PostMapping("/avaliar")
    public ResponseEntity<Object> avaliarRestaurante(@RequestParam Long idRestaurante,
                                                     @RequestParam Long idUsuario,
                                                     @Valid @RequestBody AvaliacaoDTO avaliacaoDTO) {
        try{
            Avaliacao avaliacaoCriada = avaliacaoService.criar(idRestaurante,
                                                                idUsuario,
                                                                AvaliacaoMapper.toEntity(avaliacaoDTO));
            return ResponseEntity.status(HttpStatus.CREATED).
                    body(AvaliacaoMapper.toDTO(avaliacaoCriada));
        }catch(IdJaExistenteException |
               RegistroNotFoundException |
               NullPointerException |
               RegraDeNegocioException e  ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Object> buscarUsuario(@PathVariable Long id) {
        try{
            Avaliacao avaliacaoEncontrada = avaliacaoService.buscarPeloId(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(AvaliacaoMapper.toDTO(avaliacaoEncontrada));
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscarAvaliacoesDoRestaurante/{id}")
    public ResponseEntity<Object> buscarAvaliacoesDoRestaurante(@PathVariable Long id) {
        try{
            List<Avaliacao> avaliacoesEncontradas = avaliacaoService.buscarTodasAvaliacoesRestaurante(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(avaliacoesEncontradas);
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
