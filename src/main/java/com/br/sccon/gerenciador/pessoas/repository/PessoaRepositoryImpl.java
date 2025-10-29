package com.br.sccon.gerenciador.pessoas.repository;

import com.br.sccon.gerenciador.pessoas.service.domain.Pessoa;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class PessoaRepositoryImpl implements PessoaRepository {

    private final ConcurrentHashMap<Long, Pessoa> pessoasMap = new ConcurrentHashMap<>();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public PessoaRepositoryImpl() {
        inicializarPessoas();
    }

    public Pessoa salvarPessoa(Pessoa pessoa) {
        return pessoasMap.put(pessoa.getId(), pessoa);
    }

    @Override
    public Optional<Pessoa> consultaPorId(Long id) {
        return Optional.ofNullable(pessoasMap.get(id));
    }

    @Override
    public Long consultaPorIdDesc() {
        return pessoasMap.keySet().stream()
                .max(Long::compare)
                .orElse(0L);
    }

    @Override
    public List<Pessoa> buscarTodasPessoasOrdenadasPorNome() {
        return pessoasMap.values()
                .stream()
                .sorted(Comparator.comparing(Pessoa::getNome))
                .collect(Collectors.toList());
    }

    @Override
    public boolean remover(Long id) {
        return pessoasMap.remove(id) != null;
    }


    public void inicializarPessoas() {
        var p1 = new Pessoa(
                1L,
                "José da Silva",
                ZonedDateTime.parse("2000-04-06T00:00:00Z", dateTimeFormatter),
                ZonedDateTime.parse("2020-05-10T00:00:00Z", dateTimeFormatter)
        );

        var p2 = new Pessoa(
                2L,
                "Maria de Fátima",
                ZonedDateTime.parse("1990-09-09T00:00:00Z", dateTimeFormatter),
                ZonedDateTime.parse("2002-04-06T00:00:00Z", dateTimeFormatter)
        );

        var p3 = new Pessoa(
                3L,
                "Marco Aurélio",
                ZonedDateTime.parse("2005-04-06T00:00:00Z", dateTimeFormatter),
                ZonedDateTime.parse("2022-04-06T00:00:00Z", dateTimeFormatter)
        );

        pessoasMap.put(p1.getId(), p1);
        pessoasMap.put(p2.getId(), p2);
        pessoasMap.put(p3.getId(), p3);
    }
}
