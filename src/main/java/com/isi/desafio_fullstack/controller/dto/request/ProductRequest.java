package com.isi.desafio_fullstack.controller.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;


public record ProductRequest(

        @Size(min = 3, max = 100, message = "Nome inválido. O nome deve ter de 3 a 100 caracteres")
        @Pattern(regexp = "^[a-zA-Zá-ú0-9\\s\\-_,.]+$",
                 message = "Nome inválido. O nome pode conter letras, números, espaços, underlines, vírgulas e ponto")
        @NotBlank(message = "Nome inválido. O campo nome precisa ser preenchido")
        String name,

        @Size(max = 300, message = "A descrição pode conter até 300 caracteres")
        String description,

        @Min(value = 0, message = "Estoque inválido. O estoque não pode ser menor que 0")
        @Max(value = 999999, message = "Estoque inválido. O estoque pode ter no máximo 999999 produtos")
        @NotNull(message = "Estoque inválido. O campo estoque precisa ser preenchido")
        Integer stock,

        @Digits(integer = 9, fraction = 2, message = "O preço do produto excede o valor máximo permitido")
        @NotNull(message = "Preço inválido. O campo valor do produto precisa ser preenchido")
        BigDecimal price) {}