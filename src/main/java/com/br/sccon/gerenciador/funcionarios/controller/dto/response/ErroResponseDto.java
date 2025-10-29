package com.br.sccon.gerenciador.funcionarios.controller.dto.response;

public record ErroResponseDto(
        int status,
        String erro,
        String mensagem
) {
}