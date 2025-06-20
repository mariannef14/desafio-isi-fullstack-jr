package com.isi.desafio_fullstack.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    private Timestamp removedAt;


    public void setAppliedAt(Timestamp appliedAt){
        this.appliedAt = Timestamp.from(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant());
    }

}