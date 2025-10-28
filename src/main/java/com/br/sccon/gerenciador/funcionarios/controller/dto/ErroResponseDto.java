package com.br.sccon.gerenciador.funcionarios.controller.dto;

public record ErroResponseDto(
        int status,
        String erro,
        String mensagem
) {}