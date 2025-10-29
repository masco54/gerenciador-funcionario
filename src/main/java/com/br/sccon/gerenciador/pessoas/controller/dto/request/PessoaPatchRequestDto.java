package com.br.sccon.gerenciador.pessoas.controller.dto.request;

import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record PessoaPatchRequestDto(
        String nome,
        @PastOrPresent(message = "A data de nascimento não pode ser futura.")
        LocalDate dataNascimento,
        @PastOrPresent(message = "A data de admissão não pode ser futura.")
        LocalDate dataAdmissao
) {
}
