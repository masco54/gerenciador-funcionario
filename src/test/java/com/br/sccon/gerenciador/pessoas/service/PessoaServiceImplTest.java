package com.br.sccon.gerenciador.pessoas.service;

import com.br.sccon.gerenciador.pessoas.controller.dto.request.PessoaRequestDto;
import com.br.sccon.gerenciador.pessoas.mapeamento.PessoaMapeamento;
import com.br.sccon.gerenciador.pessoas.repository.PessoaRepository;
import com.br.sccon.gerenciador.pessoas.service.domain.Pessoa;
import com.br.sccon.gerenciador.pessoas.service.exception.ConflitoException;
import com.br.sccon.gerenciador.pessoas.service.exception.NaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceImplTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @InjectMocks
    private PessoaServiceImpl pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private PessoaMapeamento pessoaMapeamento;

    private Pessoa pessoaMock;
    private PessoaRequestDto pessoaRequestMock;

    private final Long ID_EXISTENTE = 1L;
    private final Long ID_NAO_EXISTENTE = 99L;

    @BeforeEach
    void setUp() {
        var dataNascimento = LocalDate.parse("2000-04-06", dateTimeFormatter);
        var dataAdmissao = LocalDate.parse("2020-05-10", dateTimeFormatter);

        pessoaMock = new Pessoa(ID_EXISTENTE, "José da Silva", dataNascimento, dataAdmissao);
        pessoaRequestMock = new PessoaRequestDto(null, "Test", dataNascimento, dataAdmissao);

    }

    @Test
    void quandoIdExistenteDeveLancarExcecaoDeConflito() {
        var requestDtoComId = new PessoaRequestDto(ID_EXISTENTE, "Test", null, null);
        when(pessoaMapeamento.requestParaDominio(requestDtoComId)).thenReturn(pessoaMock);
        when(pessoaRepository.consultaPorId(ID_EXISTENTE)).thenReturn(Optional.of(pessoaMock));

        assertThrows(ConflitoException.class, () -> pessoaService.salvar(requestDtoComId));
        verify(pessoaRepository, never()).salvarPessoa(any(Pessoa.class));
    }

    @Test
    void quandoNaoEnviarIdDeverSerGeradoAutomaticamenteEsalvar() {
        Long maiorIdAtual = 5L;
        Long novoIdEsperado = 6L;

        pessoaMock.setId(null);

        when(pessoaMapeamento.requestParaDominio(any())).thenReturn(pessoaMock);
        when(pessoaRepository.consultaPorIdDesc()).thenReturn(maiorIdAtual);

        pessoaService.salvar(pessoaRequestMock);

        verify(pessoaRepository).consultaPorIdDesc();
        verify(pessoaRepository).salvarPessoa(argThat(pessoa -> pessoa.getId().equals(novoIdEsperado)));
    }

    @Test
    void quandoConsultarPessoaENaoEncontrarDeveLancarExcecaoNaoEncontrado() {
        when(pessoaRepository.consultaPorId(ID_NAO_EXISTENTE)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> pessoaService.deletar(ID_NAO_EXISTENTE));
        verify(pessoaRepository, never()).remover(anyLong());
    }

    @ParameterizedTest
    @MethodSource("formatosDeOutputParaCalculos")
    void quandoCalcularIdadePessoaENaoEncontrarPessoaDeveLancarExcecaoNaoEncontrado(String output) {
        when(pessoaRepository.consultaPorId(ID_NAO_EXISTENTE)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> pessoaService.calcularIdade(ID_NAO_EXISTENTE, output));
    }

    static Stream<String> formatosDeOutputParaCalculos() {
        return Stream.of("days", "months", "years");
    }


}
