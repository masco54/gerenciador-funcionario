package com.br.sccon.gerenciador.pessoas.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PessoaIdadeResponseDto(
        Long id,
        String nome,
        Long idadeEmDias,
        Long idadeEmMeses,
        Long idadeEmAnos
) {

}