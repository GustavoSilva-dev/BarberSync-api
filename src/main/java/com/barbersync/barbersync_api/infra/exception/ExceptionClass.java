package com.barbersync.barbersync_api.infra.exception;

import com.barbersync.barbersync_api.Usuarios.dtos.DadosErro;
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

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<DadosErro> handleToken(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DadosErro(message));
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
    public ResponseEntity<DadosErro> handleUsuarioNotFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosErro(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DadosErro> handleGenerics(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DadosErro("Erro de servidor: Acesso inválido ou Falha de Microsserviços"));
    }
}
