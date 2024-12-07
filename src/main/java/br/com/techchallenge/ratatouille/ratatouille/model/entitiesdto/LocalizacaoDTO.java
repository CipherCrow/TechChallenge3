package br.com.techchallenge.ratatouille.ratatouille.model.entitiesdto;

public record LocalizacaoDTO (
    Long idLocalizacao,
    String estado,
    String cidade,
    String bairro,
    String rua,
    String numero
){}
