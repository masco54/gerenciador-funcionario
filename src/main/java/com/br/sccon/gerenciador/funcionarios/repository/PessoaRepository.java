package com.br.sccon.gerenciador.funcionarios.repository;

import com.br.sccon.gerenciador.funcionarios.service.domain.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository {

    List<Pessoa> buscarTodasPessoasOrdenadasPorNome();

    Pessoa salvarPessoa(Pessoa pessoa);

    Optional<Pessoa> consultaPorId(Long id);

    Long consultaPorIdDesc();

    boolean remover(Long id);

}
