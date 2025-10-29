package com.br.sccon.gerenciador.funcionarios.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PessoaIdadeResponseDto(
        String id,
        String nome,
        Long idadeEmDias,
        Long idadeEmMeses,
        Long idadeEmAnos
) {

}