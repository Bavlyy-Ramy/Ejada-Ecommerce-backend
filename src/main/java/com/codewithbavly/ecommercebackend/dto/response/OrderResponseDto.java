package com.codewithbavly.ecommercebackend.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long id;

    private LocalDateTime orderDate;

    private String status;

    private Double totalAmount;

    private List<OrderItemResponseDto> items;

}