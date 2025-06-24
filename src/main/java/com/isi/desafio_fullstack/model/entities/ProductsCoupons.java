package com.isi.desafio_fullstack.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@Entity
@NoArgsConstructor
@Getter
public class ProductsCoupons {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products productId;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupons couponId;

    private Timestamp appliedAt;

    @Setter
    private Timestamp removedAt = null;


    public ProductsCoupons(Products productId, Coupons couponId) {
        this.productId = productId;
        this.couponId = couponId;
    }


    @PrePersist
    public void prePersist(){
        this.appliedAt = Timestamp.from(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant());
    }

}