package com.isi.desafio_fullstack.config.exceptions;

import lombok.Getter;


@Getter
public class ErrorsValidations {

    private String field;

    private String message;


    public ErrorsValidations(String field, String message) {

        this.field = field;
        this.message = message;

    }

}