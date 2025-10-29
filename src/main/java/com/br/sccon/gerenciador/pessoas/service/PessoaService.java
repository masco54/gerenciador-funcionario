package com.br.sccon.gerenciador.pessoas.service;

import com.br.sccon.gerenciador.pessoas.controller.dto.request.PessoaPatchRequestDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.request.PessoaPutRequestDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.request.PessoaRequestDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.response.PessoaIdadeResponseDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.response.PessoaSalarioResponseDto;
import com.br.sccon.gerenciador.pessoas.service.domain.Pessoa;

public interface PessoaService {

    Pessoa salvar(PessoaRequestDto requestDto);

    void deletar(Long id);

    Pessoa atualizarTotal(Long id, PessoaPutRequestDto requestDto);

    Pessoa atualizarParcial(Long id, PessoaPatchRequestDto requestDto);

    Pessoa consultarPessoaPorId(Long id);

    PessoaIdadeResponseDto calcularIdade(Long id, String output);

    PessoaSalarioResponseDto calcularSalario(Long id, String output);

}
