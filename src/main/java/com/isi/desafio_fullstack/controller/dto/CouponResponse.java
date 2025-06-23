package com.isi.desafio_fullstack.controller.dto;

import com.isi.desafio_fullstack.model.entities.Coupons;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;


@Builder
public record CouponResponse(

        String code,

        String typeCoupon,

        BigDecimal price,

        Boolean oneShot,

        LocalDateTime validFrom,

        LocalDateTime validUntil) {


    public static CouponResponse from(Coupons coupons){

        return CouponResponse.builder()
                .code(coupons.getCode())
                .typeCoupon(coupons.getType().toString())
                .price(coupons.getPrice().setScale(2, RoundingMode.HALF_EVEN))
                .oneShot(coupons.getOneShot())
                .validFrom(coupons.getValidFrom())
                .validUntil(coupons.getValidUntil())
                .build();

    }

}