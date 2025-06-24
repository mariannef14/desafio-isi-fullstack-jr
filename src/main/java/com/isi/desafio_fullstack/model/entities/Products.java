package com.isi.desafio_fullstack.model.entities;

import com.isi.desafio_fullstack.utils.UtilsCodes;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@NoArgsConstructor
@Getter
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    private Integer id;

    @Column(unique = true)
    @Size(min = 3, max = 100)
    @NotBlank
    @Setter
    private String name;

    @Size(max = 300)
    private String description;

    @Column(precision = 9, scale = 2)
    @NotNull
    @Setter
    private BigDecimal price;

    @Column(precision = 9, scale = 2)
    @Setter
    private BigDecimal finalPrice;

    @Min(0)
    @Max(999999)
    @NotNull
    private Integer stock;

    @Setter
    private Boolean isOutOfStock;

    @Setter
    private Boolean isActive = true;

    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime updatedAt;

    @Setter
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "productId")
    private List<ProductsCoupons> discount;


    public Products(String name, String description, BigDecimal price, Integer stock) {

        this.name = UtilsCodes.formatName(name);
        this.description = description;
        this.price = price.abs();
        this.stock = stock;
        this.isOutOfStock = stock == 0;

    }


    @PrePersist
    private void prePersist(){
        this.createdAt = UtilsCodes.dateToday();
    }

}