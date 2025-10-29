package com.br.sccon.gerenciador.funcionarios.controller.dto.response;

import java.time.ZonedDateTime;

public record PessoaResponseDto(Long id,
                                String nome,
                                ZonedDateTime dataNascimento,
                                ZonedDateTime dataAdmissao
) {
}