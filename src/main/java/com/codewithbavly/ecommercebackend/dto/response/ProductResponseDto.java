package com.codewithbavly.ecommercebackend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stockQuantity;
}