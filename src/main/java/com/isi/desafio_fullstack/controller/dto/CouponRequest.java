package com.isi.desafio_fullstack.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record CouponRequest(

        @Size(min = 4, max = 20, message = "Nome inválido. O nome deve ter de 4 a 20 caracteres, não pode conter acento nem espaço")
        @Pattern(regexp = "^[a-zA-Z0-9]+$")
        @NotBlank(message = "Nome inválido. O campo nome precisa ser preenchido")
        String code,

        @NotBlank(message = "Tipo inválido. O campo tipo precisa ser preenchido")
        String type,

        @NotNull(message = "Valor inválido. O campo valor precisa ser preenchido")
        BigDecimal price,

        @NotNull(message = "Tipo inválido. O campo oneShot precisa ser preenchido")
        Boolean oneShot,

        Integer maxUses,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        @NotNull(message = "Data inválida. O campo válido a partir de precisa ser preenchido")
        LocalDateTime validFrom,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        @NotNull(message = "Data inválida. O campo válido até ser preenchido")
        LocalDateTime validUntil) {}