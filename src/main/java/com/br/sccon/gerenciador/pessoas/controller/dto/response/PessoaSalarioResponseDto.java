package com.br.sccon.gerenciador.pessoas.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PessoaSalarioResponseDto(
        Long id,
        String nome,
        String salario
) {

}