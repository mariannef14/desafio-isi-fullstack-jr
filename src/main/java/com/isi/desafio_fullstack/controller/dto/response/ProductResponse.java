package com.isi.desafio_fullstack.controller.dto.response;

import com.isi.desafio_fullstack.model.entities.Products;
import lombok.Builder;

import java.math.BigDecimal;


@Builder
public record ProductResponse(

        String name,
        String description,
        Integer stock,
        Boolean isOutOfStock,
        BigDecimal price,
        Boolean isActive){


    public static ProductResponse from(Products products){

        return ProductResponse.builder()
                        .name(products.getName().toUpperCase())
                        .description(products.getDescription())
                        .stock(products.getStock())
                        .description(products.getDescription())
                        .isOutOfStock(products.getIsOutOfStock())
                        .price(products.getPrice())
                        .isActive(products.getIsActive())
                        .build();

    }

}
