package com.br.sccon.gerenciador.pessoas.repository;

import com.br.sccon.gerenciador.pessoas.service.domain.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository {

    List<Pessoa> buscarTodasPessoasOrdenadasPorNome();

    Pessoa salvarPessoa(Pessoa pessoa);

    Optional<Pessoa> consultaPorId(Long id);

    Long consultaPorIdDesc();

    boolean remover(Long id);

}
