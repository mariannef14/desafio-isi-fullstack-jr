package com.isi.desafio_fullstack.controller.query;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class ListQueryParameter {

    String search;

    BigDecimal maxPrice;

    BigDecimal minPrice;

    Boolean hasDiscount;

    String sortBy;

    String sortOrder = "asc";

    Boolean includeDeleted = false;

    Boolean onlyOutOfStock;

}