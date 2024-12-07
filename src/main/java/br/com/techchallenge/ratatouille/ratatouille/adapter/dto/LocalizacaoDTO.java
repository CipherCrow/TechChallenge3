package br.com.techchallenge.ratatouille.ratatouille.adapter.dto;

public record LocalizacaoDTO (
    Long idLocalizacao,
    String estado,
    String cidade,
    String bairro,
    String rua,
    String numero
){}
