package com.barbersync.barbersync_api.infra.exception;

import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosErro;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestControllerAdvice
public class ExceptionClass {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<DadosErro> handleBadRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DadosErro("Requisição inválida."));
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<DadosErro> handleValidacaoRequest(ValidacaoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DadosErro(ex.getMessage()));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<DadosErro> handleToken(TokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DadosErro(ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<DadosErro> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosErro(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErro>> handleValidation(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();

        var respostas = erros.stream().map(erro -> new DadosErro(erro.getDefaultMessage())).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respostas);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<DadosErro> handleConstraintViolation() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DadosErro("Erro de validação"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DadosErro> handleMessageNotReadable() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DadosErro("Corpo da requisição inválido."));
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<DadosErro> handleUsuarioNotFound(UsuarioNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosErro(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DadosErro> handleGenerics(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DadosErro("Erro de servidor: Acesso inválido ou Falha de Microsserviços"));
    }
}
