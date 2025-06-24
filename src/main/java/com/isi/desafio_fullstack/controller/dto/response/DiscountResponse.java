package com.isi.desafio_fullstack.controller.dto.response;

import com.isi.desafio_fullstack.model.entities.ProductsCoupons;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Builder
public record DiscountResponse(

        String type,
        BigDecimal value,
        LocalDateTime appliedAt) {


    public static DiscountResponse from(ProductsCoupons productsCoupons){

        return DiscountResponse.builder()
                .type(productsCoupons.getCouponId().getType().toString())
                .value(productsCoupons.getCouponId().getPrice())
                .appliedAt(productsCoupons.getAppliedAt().toLocalDateTime())
                .build();

    }

}