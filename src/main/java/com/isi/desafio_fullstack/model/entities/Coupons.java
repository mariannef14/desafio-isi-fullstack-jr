package com.isi.desafio_fullstack.model.entities;

import com.isi.desafio_fullstack.model.enums.TypeDiscountEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.TimeZone;


@Entity
@NoArgsConstructor
@AllArgsConstructor
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
    @NotBlank
    private TypeDiscountEnum type;

    private BigDecimal price;

    @NotBlank
    private Boolean oneShot;

    private Integer maxUses;

    @NotNull
    private Integer usesCount;

    @NotNull
    private LocalDateTime validFrom;

    @NotNull
    private LocalDateTime validUntil;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:MM:ss")
    private LocalDateTime deletedAt;


    @PrePersist
    private void prePersist(){
        this.createdAt = LocalDateTime.now(TimeZone.getTimeZone("America/Sao_Paulo").toZoneId());
    }

    @PreUpdate
    private void preUpdate(){
        this.updatedAt = LocalDateTime.now(TimeZone.getTimeZone("America/Sao_Paulo").toZoneId());
    }

    @PreRemove
    private void preRemove(){
        this.deletedAt = LocalDateTime.now(TimeZone.getTimeZone("America/Sao_Paulo").toZoneId());
    }

}