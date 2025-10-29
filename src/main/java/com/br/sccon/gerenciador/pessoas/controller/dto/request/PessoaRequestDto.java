package com.br.sccon.gerenciador.pessoas.controller.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record PessoaRequestDto(
        @Min(value = 1, message = "O ID deve ser um número positivo maior que 0")
        Long id,
        @NotBlank(message = "O nome é obrigatório e não pode ser vazio.")
        String nome,
        @NotNull(message = "A data de nascimento é obrigatória.")
        @PastOrPresent(message = "A data de nascimento não pode ser futura.")
        LocalDate dataNascimento,
        @NotNull(message = "A data de admissão é obrigatória.")
        @PastOrPresent(message = "A data de admissão não pode ser futura.")
        LocalDate dataAdmissao
) {
}