package com.isi.desafio_fullstack.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    private Integer id;

    @Column(unique = true)
    @Size(min = 3, max = 100)
    @NotBlank
    private String name;

    @Size(max = 300)
    private String description;

    @Digits(integer = 6, fraction = 2)
    @NotNull
    private BigDecimal price;

    @Min(0)
    @Max(999999)
    @NotNull
    private Integer stock;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "productId")
    private List<ProductsCoupons> discount;


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