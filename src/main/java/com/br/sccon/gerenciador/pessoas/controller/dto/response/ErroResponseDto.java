package com.br.sccon.gerenciador.pessoas.controller.dto.response;

public record ErroResponseDto(
        int status,
        String erro,
        String mensagem
) {
}