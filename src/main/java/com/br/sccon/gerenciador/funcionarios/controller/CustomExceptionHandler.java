package com.br.sccon.gerenciador.funcionarios.controller;

import com.br.sccon.gerenciador.funcionarios.controller.dto.response.ErroResponseDto;
import com.br.sccon.gerenciador.funcionarios.service.exception.ConflitoException;
import com.br.sccon.gerenciador.funcionarios.service.exception.FormatoSaidaInvalidoException;
import com.br.sccon.gerenciador.funcionarios.service.exception.NaoEncontradoException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResponseDto> handleIdConflitoException(ConflitoException ex) {
        var erro = new ErroResponseDto(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(erro, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({FormatoSaidaInvalidoException.class})
    public ResponseEntity<ErroResponseDto> handleFormatoSaidaInvalido(FormatoSaidaInvalidoException ex) {
        var erro = new ErroResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NaoEncontradoException.class, NoResourceFoundException.class})
    public ResponseEntity<ErroResponseDto> handleRecursoNaoEncontradoException(Exception ex) {
        var erro = new ErroResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErroResponseDto> exceptionGeral(Exception ex) {
        var erro = new ErroResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodNotAllowedException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErroResponseDto> handleMethoNotAllowedException(Exception ex) {
        var erro = new ErroResponseDto(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(erro, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var erros = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        var erro = new ErroResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Validação",
                erros
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErroResponseDto> handleInvalidFormatException(InvalidFormatException ex) {
        var campo = ex.getPath().stream()
                .map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "")
                .collect(Collectors.joining("."));

        var erro = new ErroResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Desserialização",
                "Falha ao converter valor para o tipo esperado. Campo: " + campo + ", Valor recebido: " + ex.getValue()
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {
        var mensagemErro = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .findFirst()
                .orElse("Erro de validação de parâmetro.");

        var erro = new ErroResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição Mal Formatada",
                mensagemErro
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErroResponseDto> handlebaRequest(Exception ex) {
        var erro = new ErroResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

}
