package com.isi.desafio_fullstack.config.exceptions.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.isi.desafio_fullstack.config.exceptions.ApiError;
import com.isi.desafio_fullstack.config.exceptions.ErrorsValidations;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiError> handlerErrorIllegalArgumentException(EntityExistsException exception){

        var formatError = ApiError
                            .builder()
                            .timestamp(LocalDateTime.now())
                            .code(HttpStatus.CONFLICT.value())
                            .status(HttpStatus.CONFLICT.name())
                            .message(exception.getMessage())
                            .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(formatError);

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handlerErrorMethodArgumentNotValidException(MethodArgumentNotValidException exception){

        var listErrorsMessages = exception.getBindingResult()
                                        .getFieldErrors()
                                        .stream()
                                        .map((fieldError -> new ErrorsValidations(fieldError.getField(),
                                                                                            fieldError.getDefaultMessage())))
                                        .collect(Collectors.toList());


        var formatError = ApiError
                            .builder()
                            .timestamp(LocalDateTime.now())
                            .code(HttpStatus.BAD_REQUEST.value())
                            .status(HttpStatus.BAD_REQUEST.name())
                            .message("Campos inválidos")
                            .validationsErrorsMessages(listErrorsMessages)
                            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formatError);

    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handlerErrorEntityNotFoundException(EntityNotFoundException exception){

        var formatError = ApiError
                .builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.name())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(formatError);

    }


    @ExceptionHandler({
            JsonPatchException.class,
            JsonProcessingException.class
    })
    public ResponseEntity<ApiError> handlerErrorJson(Exception exception){

        var formatError = ApiError
                .builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formatError);

    }

}