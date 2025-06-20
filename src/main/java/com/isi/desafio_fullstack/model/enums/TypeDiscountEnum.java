package com.isi.desafio_fullstack.model.enums;


public enum TypeDiscountEnum {

    PERCENT("percent"),
    FIXED("fixed");


    private String type;

    TypeDiscountEnum(String type){
        this.type = type;
    }

}