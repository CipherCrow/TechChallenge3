package br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions;

public class CampoObrigatorioNuloException extends RuntimeException {
    public CampoObrigatorioNuloException(String nomeCampo) {
        super(nomeCampo + " não pode estar nulo!");
    }
}
