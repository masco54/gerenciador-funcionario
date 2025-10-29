package com.br.sccon.gerenciador.pessoas.controller.dto.response;

import java.time.LocalDate;

public record PessoaResponseDto(Long id,
                                String nome,
                                LocalDate dataNascimento,
                                LocalDate dataAdmissao
) {
}