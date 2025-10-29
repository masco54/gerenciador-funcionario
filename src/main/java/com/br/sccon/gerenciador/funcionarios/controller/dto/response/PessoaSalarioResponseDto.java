package com.br.sccon.gerenciador.funcionarios.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PessoaSalarioResponseDto(
        Long id,
        String nome,
        String salario
) {

}