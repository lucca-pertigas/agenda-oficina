package com.oficina.agenda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, String>> tratarRegraNegocio(
            RegraNegocioException exception) {

        Map<String, String> erro = new HashMap<>();

        erro.put("mensagem", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro);
    }

    @ExceptionHandler(ConflitoAgendamentoException.class)
    public ResponseEntity<Map<String, String>> tratarConflitoAgendamento(
            ConflitoAgendamentoException exception) {

        Map<String, String> erro = new HashMap<>();

        erro.put("mensagem", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(erro);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException exception) {

        Map<String, String> erro = new HashMap<>();

        erro.put("mensagem", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(
            MethodArgumentNotValidException exception) {

        Map<String, String> erros = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(erro ->
                        erros.put(
                                erro.getField(),
                                erro.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erros);
    }
}