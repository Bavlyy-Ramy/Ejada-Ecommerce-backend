package com.codewithbavly.ecommercebackend.service;

import com.codewithbavly.ecommercebackend.dto.request.ProductRequestDto;
import com.codewithbavly.ecommercebackend.dto.response.ProductResponseDto;
import com.codewithbavly.ecommercebackend.entity.Product;
import com.codewithbavly.ecommercebackend.exception.ResourceNotFoundException;
import com.codewithbavly.ecommercebackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private  ProductRepository productRepository;

    public List<ProductResponseDto> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public ProductResponseDto getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        return mapToResponseDto(product);
    }

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {

        Product product = mapToEntity(requestDto);

        Product savedProduct = productRepository.save(product);

        return mapToResponseDto(savedProduct);
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(requestDto.getName());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setStockQuantity(requestDto.getStockQuantity());

        Product updatedProduct = productRepository.save(product);

        return mapToResponseDto(updatedProduct);
    }

    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
    }
    // Mapping Methods
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
