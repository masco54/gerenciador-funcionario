package com.br.sccon.gerenciador.funcionarios.service;

import com.br.sccon.gerenciador.funcionarios.controller.dto.PessoaPatchRequestDto;
import com.br.sccon.gerenciador.funcionarios.controller.dto.PessoaPutRequestDto;
import com.br.sccon.gerenciador.funcionarios.controller.dto.PessoaRequestDto;
import com.br.sccon.gerenciador.funcionarios.mapeamento.PessoaMapeamento;
import com.br.sccon.gerenciador.funcionarios.repository.PessoaRepository;
import com.br.sccon.gerenciador.funcionarios.service.domain.Pessoa;
import com.br.sccon.gerenciador.funcionarios.service.exception.ConflitoException;
import com.br.sccon.gerenciador.funcionarios.service.exception.NaoEncontradoException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaServiceImpl implements PessoaService {

    @Autowired
    private PessoaMapeamento pessoaMapeamento;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Override
    public Pessoa consultarPessoaPorId(Long id) {
        return pessoaRepository.consultaPorId(id)
                .orElseThrow(() -> new NaoEncontradoException("Pessoa com ID " + id + " não encontrada."));
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
