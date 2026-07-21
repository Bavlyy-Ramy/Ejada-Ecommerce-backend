package com.codewithbavly.ecommercebackend.mapper;

import com.codewithbavly.ecommercebackend.dto.request.ProductRequestDto;
import com.codewithbavly.ecommercebackend.dto.response.ProductResponseDto;
import com.codewithbavly.ecommercebackend.entity.Product;

public class mapper {
    private ProductResponseDto mapToResponseDto(Product product) {

        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();
    }
    private Product mapToEntity(ProductRequestDto dto) {

        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .build();
    }
}
