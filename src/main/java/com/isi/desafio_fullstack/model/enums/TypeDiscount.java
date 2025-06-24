package com.isi.desafio_fullstack.model.enums;

import lombok.Getter;


@Getter
public enum TypeDiscount {

    QUEIMA_DE_ESTOQUE(0.5, true),
    CAMPANHAS_SAZONAIS(0.3, false);

    private Double discount;
    private Boolean isActive;


    TypeDiscount(Double discount, Boolean isActive){
        this.discount = discount;
        this.isActive = isActive;
    }

}