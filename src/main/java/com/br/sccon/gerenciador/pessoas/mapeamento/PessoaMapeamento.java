package com.br.sccon.gerenciador.pessoas.mapeamento;

import com.br.sccon.gerenciador.pessoas.controller.dto.request.PessoaRequestDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.response.PessoaResponseDto;
import com.br.sccon.gerenciador.pessoas.service.domain.Pessoa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PessoaMapeamento {

    Pessoa requestParaDominio(PessoaRequestDto request);

    PessoaResponseDto dominioParaResponse(Pessoa pessoa);
}
