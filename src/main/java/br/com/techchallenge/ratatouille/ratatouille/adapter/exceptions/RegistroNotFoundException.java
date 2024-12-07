package br.com.techchallenge.ratatouille.ratatouille.adapter.exceptions;

public class RegistroNotFoundException extends RuntimeException {
    public RegistroNotFoundException(String tabela,Long id) {
        super(tabela + " não encontrado com ID: " + id);
    }
}
