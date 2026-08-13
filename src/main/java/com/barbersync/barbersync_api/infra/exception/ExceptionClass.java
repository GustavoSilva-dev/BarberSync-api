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

@RestControllerAdvice
public class ExceptionClass {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<DadosErro> handleBadRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DadosErro("Requisição inválida."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DadosErro> handleValidation() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DadosErro("Erro de validação"));
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
    public ResponseEntity<DadosErro> handleUsuarioNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosErro("Usuário não encontrado."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DadosErro> handleGeneric() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DadosErro("Erro interno no servidor."));
    }
}
