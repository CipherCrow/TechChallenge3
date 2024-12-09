package br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions;

public class RegraDeNegocioException extends RuntimeException{
    public RegraDeNegocioException(String motivo){
        super(motivo);
    }
}
