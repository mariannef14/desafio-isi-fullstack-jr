package com.isi.desafio_fullstack.model.entities;

import com.isi.desafio_fullstack.model.enums.TypeCouponEnum;
import com.isi.desafio_fullstack.utils.UtilsCodes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@NoArgsConstructor
@Getter
public class Coupons {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    private Integer id;

    @Size(min = 4, max = 20)
    @NotBlank
    private String code;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TypeCouponEnum type;

    private BigDecimal price;

    @NotNull
    private Boolean oneShot;

    private Integer maxUses;

    private Integer usesCount;

    @NotNull
    private LocalDateTime validFrom;

    @NotNull
    private LocalDateTime validUntil;

    @Setter
    private Boolean isActive;

    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime updatedAt;

    @Setter
    private LocalDateTime deletedAt;


    public Coupons(String code, String typeCounpon, BigDecimal price, Boolean oneShot, Integer maxUses,
                   LocalDateTime validFrom, LocalDateTime validUntil) {

        this.code = code;
        this.type = TypeCouponEnum.valueOf(typeCounpon.trim().toUpperCase());
        this.price = price;
        this.oneShot = oneShot;
        this.maxUses = Objects.isNull(maxUses) ? null: maxUses;
        this.validFrom = validFrom;
        this.validUntil = validUntil;

    }


    public Coupons(String typeCounpon, BigDecimal price, Boolean oneShot, Integer maxUses,
                   LocalDateTime validFrom, LocalDateTime validUntil) {

        this.code = this.getCode();
        this.type = TypeCouponEnum.valueOf(typeCounpon.trim().toUpperCase());
        this.price = price;
        this.oneShot = oneShot;
        this.maxUses = Objects.isNull(maxUses)? null: maxUses;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.updatedAt = UtilsCodes.dateToday();

    }


    @PrePersist
    private void prePersist(){
        this.createdAt = UtilsCodes.dateToday();
    }

}