package com.isi.desafio_fullstack.config.exceptions;


public class UnprocessableEntityException extends RuntimeException {

    public UnprocessableEntityException(String message){
        super(message);
    }

}