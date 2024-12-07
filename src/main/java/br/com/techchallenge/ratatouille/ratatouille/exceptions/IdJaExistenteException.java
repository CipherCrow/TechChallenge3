package br.com.techchallenge.ratatouille.ratatouille.exceptions;

public class IdJaExistenteException extends RuntimeException{
    public IdJaExistenteException(String texto){
        super(texto);
    }
}
