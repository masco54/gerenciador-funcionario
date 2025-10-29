package com.br.sccon.gerenciador.funcionarios.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.ZonedDateTime;

public record PessoaRequestDto(Long id,
                               @NotBlank(message = "O nome é obrigatório e não pode ser vazio.")
                               String nome,
                               @NotNull(message = "A data de nascimento é obrigatória.")
                               @PastOrPresent(message = "A data de nascimento não pode ser futura.")
                               ZonedDateTime dataNascimento,
                               @NotNull(message = "A data de admissão é obrigatória.")
                               @PastOrPresent(message = "A data de admissão não pode ser futura.")
                               ZonedDateTime dataAdmissao
) {
}