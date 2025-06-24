package com.isi.desafio_fullstack.controller.dto.response;

import com.isi.desafio_fullstack.model.entities.Products;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;


@Builder
public record ProductDiscountResponse(

        Integer id,
        String name,
        String description,
        Integer stock,
        Boolean isOutOfStock,
        BigDecimal price,
        BigDecimal finalPrice,
        DiscountResponse discount,
        Boolean hasCouponApplied,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static ProductDiscountResponse from(Products products){

        var hasDiscount = products.getDiscount().stream()
                        .filter(productsCoupons -> productsCoupons.getRemovedAt() == null)
                        .findFirst();

        return ProductDiscountResponse.builder()
                .id(products.getId())
                .name(products.getName().toUpperCase())
                .description(products.getDescription())
                .stock(products.getStock())
                .description(products.getDescription())
                .isOutOfStock(products.getIsOutOfStock())
                .price(products.getPrice().setScale(2, RoundingMode.HALF_EVEN))
                .finalPrice(Objects.isNull(products.getFinalPrice()) ? BigDecimal.ZERO : products.getFinalPrice()
                        .setScale(2, RoundingMode.HALF_EVEN))
                .discount(hasDiscount.isPresent() ? DiscountResponse.from(hasDiscount.get()) : null)
                .hasCouponApplied(hasDiscount.isPresent())
                .createdAt(products.getCreatedAt())
                .updatedAt(products.getUpdatedAt())
                .build();

    }

}