package com.br.sccon.gerenciador.funcionarios.service;

import com.br.sccon.gerenciador.funcionarios.controller.dto.request.PessoaPatchRequestDto;
import com.br.sccon.gerenciador.funcionarios.controller.dto.request.PessoaPutRequestDto;
import com.br.sccon.gerenciador.funcionarios.controller.dto.request.PessoaRequestDto;
import com.br.sccon.gerenciador.funcionarios.controller.dto.response.PessoaIdadeResponseDto;
import com.br.sccon.gerenciador.funcionarios.mapeamento.PessoaMapeamento;
import com.br.sccon.gerenciador.funcionarios.repository.PessoaRepository;
import com.br.sccon.gerenciador.funcionarios.service.domain.Pessoa;
import com.br.sccon.gerenciador.funcionarios.service.exception.ConflitoException;
import com.br.sccon.gerenciador.funcionarios.service.exception.FormatoSaidaInvalidoException;
import com.br.sccon.gerenciador.funcionarios.service.exception.NaoEncontradoException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PessoaServiceImpl implements PessoaService {

    private static final Set<String> FORMATOS_VALIDOS = Set.of("days", "months", "years");

    @Autowired
    private PessoaMapeamento pessoaMapeamento;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Override
    public Pessoa consultarPessoaPorId(Long id) {
        return pessoaRepository.consultaPorId(id).orElseThrow(() -> new NaoEncontradoException("Pessoa com ID " + id + " não encontrada."));
    }

    @Override
    public PessoaIdadeResponseDto calcularIdade(Long id, String output) {
        var parametros = Arrays.asList(output.toLowerCase().split("\\|"));

        validaFormatoOutput(parametros);

        var pessoa = pessoaRepository.consultaPorId(id).orElseThrow(() -> new NaoEncontradoException("Pessoa com ID " + id + " não encontrada."));

        var dataNascimento = pessoa.getDataNascimento().toLocalDate();
        var hoje = LocalDate.now();

        Long totalDias = parametros.stream().anyMatch(p -> p.equals("days")) ? ChronoUnit.DAYS.between(dataNascimento, hoje) : null;
        Long totalMeses = parametros.stream().anyMatch(p -> p.equals("months")) ? ChronoUnit.MONTHS.between(dataNascimento, hoje) : null;
        Long totalAnos = parametros.stream().anyMatch(p -> p.equals("years")) ? ChronoUnit.YEARS.between(dataNascimento, hoje) : null;

        return new PessoaIdadeResponseDto(
                pessoa.getId().toString(),
                pessoa.getNome(),
                totalDias,
                totalMeses,
                totalAnos
        );
    }

    private static void validaFormatoOutput(List<String> parametros) {
        var possuiParametrosInvalidos = parametros
                .stream()
                .filter(p -> !FORMATOS_VALIDOS.contains(p))
                .collect(Collectors.toList());

        if (!possuiParametrosInvalidos.isEmpty()) {
            var mensagem = String.format("Formato(s) de saída inválido(s): %s. Use os seguintes formatos: %s.",
                    String.join(", ", possuiParametrosInvalidos),
                    String.join(", ", FORMATOS_VALIDOS));
            throw new FormatoSaidaInvalidoException(mensagem);
        }
    }

    @Override
    public Pessoa salvar(PessoaRequestDto pessoaRequestDto) {

        var pessoa = pessoaMapeamento.requestParaDominio(pessoaRequestDto);

        if (pessoa.getId() != null) {
            if (pessoaRepository.consultaPorId(pessoa.getId()).isPresent()) {
                throw new ConflitoException("Já existe uma pessoa com o id " + pessoa.getId());
            }
        } else {
            var idMaior = pessoaRepository.consultaPorIdDesc();
            pessoa.setId(idMaior + 1L);
        }

        return pessoaRepository.salvarPessoa(pessoa);
    }

    @Override
    public void deletar(Long id) {
        if (pessoaRepository.consultaPorId(id).isEmpty()) {
            throw new NaoEncontradoException("Pessoa com id " + id + " não encontrada");
        }

        var removido = pessoaRepository.remover(id);

        if (!removido) {
            throw new NaoEncontradoException("Falha ao deletar a pessoa com ID " + id + " não encontrada.");
        }
    }

    @Override
    public Pessoa atualizarTotal(Long id, PessoaPutRequestDto requestDto) {

        var pessoa = pessoaRepository.consultaPorId(id);

        if (pessoa.isEmpty()) {
            throw new NaoEncontradoException("Não foi possível atualizar: Pessoa com ID " + id + " não encontrada.");
        }

        var pessoaEncontrada = pessoa.get();

        pessoaEncontrada.setNome(requestDto.nome());
        pessoaEncontrada.setDataAdmissao(requestDto.dataAdmissao());
        pessoaEncontrada.setDataNascimento(requestDto.dataNascimento());

        return pessoaRepository.salvarPessoa(pessoaEncontrada);
    }

    @Override
    public Pessoa atualizarParcial(Long id, PessoaPatchRequestDto requestDto) {

        var pessoa = pessoaRepository.consultaPorId(id);

        if (pessoa.isEmpty()) {
            throw new NaoEncontradoException("Não foi possível atualizar: Pessoa com ID " + id + " não encontrada.");
        }

        var pessoaEncontrada = pessoa.get();

        if (StringUtils.isNotBlank(requestDto.nome())) {
            pessoaEncontrada.setNome(requestDto.nome());
        }

        if (requestDto.dataAdmissao() != null) {
            pessoaEncontrada.setDataAdmissao(requestDto.dataAdmissao());
        }

        if (requestDto.dataNascimento() != null) {
            pessoaEncontrada.setDataNascimento(requestDto.dataNascimento());
        }

        return pessoaRepository.salvarPessoa(pessoaEncontrada);
    }

}
