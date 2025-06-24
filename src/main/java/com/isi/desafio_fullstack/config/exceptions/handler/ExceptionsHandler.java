package com.isi.desafio_fullstack.config.exceptions.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.isi.desafio_fullstack.config.exceptions.ApiError;
import com.isi.desafio_fullstack.config.exceptions.CouponNotActiveException;
import com.isi.desafio_fullstack.config.exceptions.ErrorsValidations;
import com.isi.desafio_fullstack.config.exceptions.UnprocessableEntityException;
import com.isi.desafio_fullstack.utils.UtilsCodes;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.DateTimeException;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiError> handlerErrorIllegalArgumentException(EntityExistsException exception){

        var formatError = ApiError
                            .builder()
                            .timestamp(UtilsCodes.dateToday())
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
                            .timestamp(UtilsCodes.dateToday())
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
                            .timestamp(UtilsCodes.dateToday())
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
                            .timestamp(UtilsCodes.dateToday())
                            .code(HttpStatus.BAD_REQUEST.value())
                            .status(HttpStatus.BAD_REQUEST.name())
                            .message(exception.getMessage())
                            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formatError);

    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handlerErrorIllegalArgumentException(IllegalArgumentException exception){

        var formatError = ApiError
                            .builder()
                            .timestamp(UtilsCodes.dateToday())
                            .code(HttpStatus.BAD_REQUEST.value())
                            .status(HttpStatus.BAD_REQUEST.name())
                            .message(exception.getMessage())
                            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formatError);

    }


    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ApiError> handlerErrorUnprocessableEntityException(UnprocessableEntityException exception){

        var formatError = ApiError
                .builder()
                .timestamp(UtilsCodes.dateToday())
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.name())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(formatError);

    }


    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<ApiError> handlerErrorDateTimeException(DateTimeException exception){

        var formatError = ApiError
                .builder()
                .timestamp(UtilsCodes.dateToday())
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formatError);

    }


    @ExceptionHandler(CouponNotActiveException.class)
    public ResponseEntity<ApiError> handlerErrorCouponNotActiveException(CouponNotActiveException exception){

        var formatError = ApiError
                .builder()
                .timestamp(UtilsCodes.dateToday())
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(formatError);

    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handlerErrorException(Exception exception){

        var formatError = ApiError
                            .builder()
                            .timestamp(UtilsCodes.dateToday())
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                            .message(exception.getMessage())
                            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(formatError);

    }

}