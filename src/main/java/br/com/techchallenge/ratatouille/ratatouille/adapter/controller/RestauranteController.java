package br.com.techchallenge.ratatouille.ratatouille.adapter.controller;

import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.HorarioDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.LocalizacaoDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.RestauranteDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.HorarioMapper;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.LocalizacaoMapper;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.RestauranteMapper;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Horario;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Restaurante;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.HorarioService;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.LocalizacaoService;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/restaurante")
public class RestauranteController {

    @Autowired
    RestauranteService restauranteService;

    @Autowired
    LocalizacaoService localizacaoService;

    @Autowired
    HorarioService horarioService;

    @PostMapping("/cadastrarRestaurante")
    public ResponseEntity<Object> criarRestaurante(@Valid @RequestBody RestauranteDTO restauranteDTO) {
        try{
            Restaurante restauranteCadastrado = restauranteService.criar(RestauranteMapper.toEntity(restauranteDTO));
            return ResponseEntity.status(HttpStatus.CREATED).body(RestauranteMapper.toDTO(restauranteCadastrado));
        }catch(IdJaExistenteException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscarRestaurante/{id}")
    public ResponseEntity<Object> buscarRestaurante(@PathVariable Long id) {
        try{
            Restaurante restauranteEncontrado = restauranteService.buscarPeloId(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(RestauranteMapper.toDTO(restauranteEncontrado));
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscarRestaurantePorNome/{nome}")
    public ResponseEntity<Object> encontrarRestaurantePorNome(@PathVariable String nome) {
        try{
            List<Restaurante> restaurantesEncontrados = restauranteService.buscarPeloNome(nome);
            return ResponseEntity.status(HttpStatus.FOUND).body(restaurantesEncontrados);
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscarRestaurantePorLocalizacao")
    public ResponseEntity<Object> encontrarRestaurantePorLocalizacao(@Valid @RequestBody LocalizacaoDTO localizacaoDTO) {
        try{
            List<Restaurante> restaurantesEncontrados = restauranteService.buscarPelaLocalizacao(LocalizacaoMapper.toEntity(localizacaoDTO));
            return ResponseEntity.status(HttpStatus.FOUND).body(restaurantesEncontrados);
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/atualizarDadosRestaurante/{id}")
    public ResponseEntity<Object> atualizarRestaurante(@PathVariable Long id,
                                                       @Valid @RequestBody RestauranteDTO restauranteDTO) {
        try{
            Restaurante restauranteAtualizado =
                    restauranteService.atualizarDados(id,
                            restauranteDTO.nome(),
                            restauranteDTO.tipoDeCozinhaEnum());
            return ResponseEntity.status(HttpStatus.OK).body(RestauranteMapper.toDTO(restauranteAtualizado));
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/atualizarLocalizacaoRestaurante/{id}")
    public ResponseEntity<Object> atualizarLocalizacao(@PathVariable Long id,
                                                       @Valid  @RequestBody LocalizacaoDTO localizacaoDTO) {
        try{
            Restaurante restauranteAtualizado =
                    restauranteService.atualizarLocalizacao(id, LocalizacaoMapper.toEntity(localizacaoDTO)   );
            return ResponseEntity.status(HttpStatus.OK).body(RestauranteMapper.toDTO(restauranteAtualizado));
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/adicionarHorario/{id}")
    public ResponseEntity<Object> adicionarHorario(@PathVariable Long id,
                                                   @Valid @RequestBody HorarioDTO horarioDTO){
        try{
            Horario horarioAdicionado = horarioService.adicionarHorarioDeFuncionamentoAoRestaurante(id,HorarioMapper.toEntity(horarioDTO));
            return ResponseEntity.status(HttpStatus.OK).body(HorarioMapper.toDTO(horarioAdicionado));
        }catch(NullPointerException | IdJaExistenteException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscarHorarioPeloId/{id}")
    public ResponseEntity<Object> buscarPeloHorario(@PathVariable Long id) {
        try{
            Horario horarioEncontrado = horarioService.buscarPeloId(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(HorarioMapper.toDTO(horarioEncontrado));
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/atualizarHorario/{id}")
    public ResponseEntity<Object> atualizarHorario(@PathVariable Long id,
                                                   @Valid @RequestBody HorarioDTO horarioDTO){
        try{
            Horario horarioAtualizado = horarioService.atualizarHorario(id,HorarioMapper.toEntity(horarioDTO));
            return ResponseEntity.status(HttpStatus.OK).body(HorarioMapper.toDTO(horarioAtualizado));
        }catch(NullPointerException | IdJaExistenteException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/atualizarCapacidadeDeReservasNoHorario/{id}")
    public ResponseEntity<Object> atualizarCapacidadeHorario(@PathVariable Long id,
                                                             @RequestParam Integer capacidade){
        try{
            Horario horarioAtualizado = horarioService.atualizarQuantidadeDeReservasMaximas(id,capacidade);
            return ResponseEntity.status(HttpStatus.OK).body(HorarioMapper.toDTO(horarioAtualizado));
        }catch(NullPointerException | IdJaExistenteException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/deletarHorario/{id}")
    public ResponseEntity<Object> deletarHorario(@PathVariable Long id){
        try{
            horarioService.deletarPeloId(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch(NullPointerException | IdJaExistenteException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
