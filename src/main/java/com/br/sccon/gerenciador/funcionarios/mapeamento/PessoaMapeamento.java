package com.br.sccon.gerenciador.funcionarios.mapeamento;

import com.br.sccon.gerenciador.funcionarios.controller.dto.PessoaRequestDto;
import com.br.sccon.gerenciador.funcionarios.controller.dto.PessoaResponseDto;
import com.br.sccon.gerenciador.funcionarios.service.domain.Pessoa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PessoaMapeamento {

    Pessoa requestParaDominio(PessoaRequestDto request);

    PessoaResponseDto dominioParaResponse(Pessoa pessoa);
}
