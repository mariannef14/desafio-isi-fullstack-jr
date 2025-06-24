package com.isi.desafio_fullstack.config.exceptions;


public class CouponNotActiveException extends RuntimeException {

    public CouponNotActiveException(String message) {
        super(message);
    }

}