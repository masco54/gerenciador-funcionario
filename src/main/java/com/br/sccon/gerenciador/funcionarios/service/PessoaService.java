package com.br.sccon.gerenciador.funcionarios.service;

import com.br.sccon.gerenciador.funcionarios.controller.dto.request.PessoaPatchRequestDto;
import com.br.sccon.gerenciador.funcionarios.controller.dto.request.PessoaPutRequestDto;
import com.br.sccon.gerenciador.funcionarios.controller.dto.request.PessoaRequestDto;
import com.br.sccon.gerenciador.funcionarios.controller.dto.response.PessoaIdadeResponseDto;
import com.br.sccon.gerenciador.funcionarios.service.domain.Pessoa;

public interface PessoaService {

    Pessoa salvar(PessoaRequestDto requestDto);

    void deletar(Long id);

    Pessoa atualizarTotal(Long id, PessoaPutRequestDto requestDto);

    Pessoa atualizarParcial(Long id, PessoaPatchRequestDto requestDto);

    Pessoa consultarPessoaPorId(Long id);

    PessoaIdadeResponseDto calcularIdade(Long id, String output);

}
