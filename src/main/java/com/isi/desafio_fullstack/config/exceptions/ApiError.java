package com.isi.desafio_fullstack.config.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    LocalDateTime timestamp;

    Integer code;

    String status;

    String message;

    List<ErrorsValidations> validationsErrorsMessages;

}