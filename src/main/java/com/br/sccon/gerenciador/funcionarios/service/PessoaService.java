package com.br.sccon.gerenciador.funcionarios.service;

import com.br.sccon.gerenciador.funcionarios.controller.dto.PessoaPatchRequestDto;
import com.br.sccon.gerenciador.funcionarios.controller.dto.PessoaPutRequestDto;
import com.br.sccon.gerenciador.funcionarios.controller.dto.PessoaRequestDto;
import com.br.sccon.gerenciador.funcionarios.service.domain.Pessoa;

public interface PessoaService {

    Pessoa salvar(PessoaRequestDto requestDto);

    void deletar(Long id);

    Pessoa atualizarTotal(Long id, PessoaPutRequestDto requestDto);

    Pessoa atualizarParcial(Long id, PessoaPatchRequestDto requestDto);

    Pessoa consultarPessoaPorId(Long id);

    String calcularIdade(Long id, String output);

}
