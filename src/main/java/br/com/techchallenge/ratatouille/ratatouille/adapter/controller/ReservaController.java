package br.com.techchallenge.ratatouille.ratatouille.adapter.controller;

import br.com.techchallenge.ratatouille.ratatouille.adapter.dto.ReservaDTO;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.IdJaExistenteException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegistroNotFoundException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions.RegraDeNegocioException;
import br.com.techchallenge.ratatouille.ratatouille.adapter.mapper.ReservaMapper;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.entities.Reserva;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.enums.StatusReservaEnum;
import br.com.techchallenge.ratatouille.ratatouille.domain.model.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    @Autowired
    ReservaService reservaService;

    @PostMapping("/realizarReserva/{idHorario}")
    public ResponseEntity<Object> realizarReserva(@PathVariable Long idHorario,
                                                  @RequestBody ReservaDTO reservaDTO) {
        try{
            Reserva reservaCriada = reservaService.adicionarReservaParaHorario(idHorario,reservaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(ReservaMapper.toDTO(reservaCriada));
        }catch(NullPointerException | RegraDeNegocioException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(IdJaExistenteException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verificarReserva/{id}")
    public ResponseEntity<Object> buscarReserva(@PathVariable Long id) {
        try{
            Reserva reservaEncontrada = reservaService.buscarPeloId(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(ReservaMapper.toDTO(reservaEncontrada));
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/cancelarReserva/{id}")
    public ResponseEntity<Object> cancelarReserva(@PathVariable Long id) {
        try{
            Reserva reservaCancelada = reservaService.cancelarPeloId(id);
            return ResponseEntity.status(HttpStatus.OK).body(ReservaMapper.toDTO(reservaCancelada));
        }catch(NullPointerException | RegraDeNegocioException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscarReservasDoHorario/{id}")
    public ResponseEntity<Object> buscarReservasDoHorario(@PathVariable Long id) {
        try{
            List<Reserva> listaDeReservas = reservaService.buscarTodasReservasDoHorario(id);
            return ResponseEntity.status(HttpStatus.OK).body(listaDeReservas);
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscarReservasDoHorarioPeloStatus/{id}")
    public ResponseEntity<Object> buscarReservasParaHorarioPeloStatus(@PathVariable Long id, @RequestParam StatusReservaEnum status) {
        try{
            List<Reserva> listaDeReservas = reservaService.buscarTodasReservasDoHorarioComStatus(id,status);
            return ResponseEntity.status(HttpStatus.OK).body(listaDeReservas);
        }catch(NullPointerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/iniciarAtendimendoReserva/{id}")
    public ResponseEntity<Object> iniciarAtendimendoReserva(@PathVariable Long id) {
        try{
            Reserva reservaSendoAtendida = reservaService.iniciarPeloId(id);
            return ResponseEntity.status(HttpStatus.OK).body(ReservaMapper.toDTO(reservaSendoAtendida));
        }catch(NullPointerException | RegraDeNegocioException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/finalizarAtendimendoReserva/{id}")
    public ResponseEntity<Object> finalizarAtendimendoReserva(@PathVariable Long id) {
        try{
            Reserva reservaFinalizada = reservaService.finalizarPeloId(id);
            return ResponseEntity.status(HttpStatus.OK).body(ReservaMapper.toDTO(reservaFinalizada));
        }catch(NullPointerException | RegraDeNegocioException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(RegistroNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
