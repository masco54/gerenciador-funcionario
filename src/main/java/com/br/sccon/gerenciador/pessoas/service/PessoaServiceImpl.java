package com.br.sccon.gerenciador.pessoas.service;

import com.br.sccon.gerenciador.pessoas.controller.dto.request.PessoaPatchRequestDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.request.PessoaPutRequestDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.request.PessoaRequestDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.response.PessoaIdadeResponseDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.response.PessoaSalarioResponseDto;
import com.br.sccon.gerenciador.pessoas.mapeamento.PessoaMapeamento;
import com.br.sccon.gerenciador.pessoas.repository.PessoaRepository;
import com.br.sccon.gerenciador.pessoas.service.domain.Pessoa;
import com.br.sccon.gerenciador.pessoas.service.exception.ConflitoException;
import com.br.sccon.gerenciador.pessoas.service.exception.FormatoSaidaInvalidoException;
import com.br.sccon.gerenciador.pessoas.service.exception.NaoEncontradoException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
public class PessoaServiceImpl implements PessoaService {

    private static final Set<String> FORMATOS_VALIDOS_PARA_IDADE = Set.of("days", "months", "years");
    private static final Set<String> FORMATOS_VALIDOS_PARA_SALARIO = Set.of("min", "full");

    private static final BigDecimal SALARIO = new BigDecimal("1558.00");
    private static final BigDecimal SALARIO_PORCENTAGEM = new BigDecimal("0.18");
    private static final BigDecimal SALARIO_ACRESCIMO_FIXO = new BigDecimal("500.00");
    private static final BigDecimal SALARIO_MINIMO_REFERENCIA = new BigDecimal("1302.00");


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

        validaFormatoOutputIdade(output);

        var pessoa = pessoaRepository.consultaPorId(id).orElseThrow(() -> new NaoEncontradoException("Pessoa com ID " + id + " não encontrada."));

        var dataNascimento = pessoa.getDataNascimento();
        var hoje = LocalDate.now();

        Long totalDias = output.equals("days") ? ChronoUnit.DAYS.between(dataNascimento, hoje) : null;
        Long totalMeses = output.equals("months") ? ChronoUnit.MONTHS.between(dataNascimento, hoje) : null;
        Long totalAnos = output.equals("years") ? ChronoUnit.YEARS.between(dataNascimento, hoje) : null;

        return new PessoaIdadeResponseDto(pessoa.getId(), pessoa.getNome(), totalDias, totalMeses, totalAnos);
    }

    @Override
    public PessoaSalarioResponseDto calcularSalario(Long id, String output) {
        validaFormatoOutputSalario(output);

        var pessoa = pessoaRepository.consultaPorId(id).orElseThrow(() -> new NaoEncontradoException("Pessoa com ID " + id + " não encontrada."));

        long anosDeServico = ChronoUnit.YEARS.between(pessoa.getDataAdmissao(), ZonedDateTime.now().toLocalDate());
        var salarioFull = calculaSalarioFull(anosDeServico);
        var salarioMin = calculaSalarioMin(salarioFull);

        var salario = output.equals("full") ? salarioFull : salarioMin;

        return new PessoaSalarioResponseDto(pessoa.getId(), pessoa.getNome(), salario.toString());

    }

    private static BigDecimal calculaSalarioMin(BigDecimal salarioTotal) {

        BigDecimal salarioEmMinimos = BigDecimal.ZERO;

        if (SALARIO_MINIMO_REFERENCIA.compareTo(BigDecimal.ZERO) > 0) {
            salarioEmMinimos = salarioTotal.divide(SALARIO_MINIMO_REFERENCIA, 2, RoundingMode.CEILING);
        }

        return salarioEmMinimos;
    }

    private static BigDecimal calculaSalarioFull(long anosDeServico) {
        var salarioTotal = SALARIO;

        for (int i = 0; i < anosDeServico; i++) {
            var acrescimoPercentual = SALARIO.multiply(SALARIO_PORCENTAGEM);

            salarioTotal = salarioTotal.add(acrescimoPercentual).add(SALARIO_ACRESCIMO_FIXO);
        }

        salarioTotal = salarioTotal.setScale(2, RoundingMode.HALF_UP);

        return salarioTotal;
    }

    private static void validaFormatoOutputIdade(String output) {
        if (!FORMATOS_VALIDOS_PARA_IDADE.contains(output)) {
            var mensagem = String.format("Formato do parametro output inválido: %s. Use os seguintes formatos: %s.", String.join(", ", output), String.join(", ", FORMATOS_VALIDOS_PARA_IDADE));
            throw new FormatoSaidaInvalidoException(mensagem);
        }
    }

    private static void validaFormatoOutputSalario(String output) {
        if (!FORMATOS_VALIDOS_PARA_SALARIO.contains(output)) {
            var mensagem = String.format("Formato do parametro output inválido: %s. Use os seguintes formatos: %s.", String.join(", ", output), String.join(", ", FORMATOS_VALIDOS_PARA_SALARIO));
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
