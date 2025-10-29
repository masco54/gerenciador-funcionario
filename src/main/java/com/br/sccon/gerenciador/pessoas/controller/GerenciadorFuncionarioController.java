package com.br.sccon.gerenciador.pessoas.controller;

import com.br.sccon.gerenciador.pessoas.controller.dto.request.PessoaPatchRequestDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.request.PessoaPutRequestDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.request.PessoaRequestDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.response.PessoaIdadeResponseDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.response.PessoaResponseDto;
import com.br.sccon.gerenciador.pessoas.controller.dto.response.PessoaSalarioResponseDto;
import com.br.sccon.gerenciador.pessoas.mapeamento.PessoaMapeamento;
import com.br.sccon.gerenciador.pessoas.repository.PessoaRepository;
import com.br.sccon.gerenciador.pessoas.service.PessoaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class GerenciadorFuncionarioController {

    private final PessoaRepository pessoaRepository;
    private final PessoaService pessoaService;
    private final PessoaMapeamento pessoaMapeamento;

    @Autowired
    public GerenciadorFuncionarioController(PessoaRepository pessoaRepository, PessoaService pessoaService, PessoaMapeamento pessoaMapeamento) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaService = pessoaService;
        this.pessoaMapeamento = pessoaMapeamento;
    }

    @GetMapping
    public ResponseEntity<List<PessoaResponseDto>> listarPessoasOrdenadas() {
        var pessoas = pessoaRepository.buscarTodasPessoasOrdenadasPorNome();
        return ResponseEntity.ok(pessoas.stream().map(pessoaMapeamento::dominioParaResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDto> listarPessoaPorId(@PathVariable Long id) {
        var pessoa = pessoaService.consultarPessoaPorId(id);
        return ResponseEntity.ok(pessoaMapeamento.dominioParaResponse(pessoa));
    }

    @GetMapping("/{id}/age")
    public ResponseEntity<PessoaIdadeResponseDto> calcularIdadePessoa(
            @PathVariable
            Long id,
            @RequestParam @NotBlank(message = "O parâmetro output é obrigatório.")
            String output) {

        var idadePessoa = pessoaService.calcularIdade(id, output);

        return ResponseEntity.ok(idadePessoa);
    }

    @GetMapping("/{id}/salary")
    public ResponseEntity<PessoaSalarioResponseDto> calcularSalarioPessoa(
            @PathVariable
            Long id,
            @RequestParam @NotBlank(message = "O parâmetro 'output' é obrigatório.")
            String output) {

        var resultado = pessoaService.calcularSalario(id, output);

        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody PessoaRequestDto pessoaRequest) {

        var pessoa = pessoaService.salvar(pessoaRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaMapeamento.dominioParaResponse(pessoa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        pessoaService.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDto> atualizacaoTotal(@PathVariable Long id, @Valid @RequestBody PessoaPutRequestDto request) {

        var pessoa = pessoaService.atualizarTotal(id, request);
        return ResponseEntity.ok(pessoaMapeamento.dominioParaResponse(pessoa));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PessoaResponseDto> atualizacaoParcial(@PathVariable Long id, @Valid @RequestBody PessoaPatchRequestDto request) {

        var pessoa = pessoaService.atualizarParcial(id, request);
        return ResponseEntity.ok(pessoaMapeamento.dominioParaResponse(pessoa));
    }

}
