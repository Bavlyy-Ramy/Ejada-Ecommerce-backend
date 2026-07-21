package com.codewithbavly.ecommercebackend.dto.request;

import com.codewithbavly.ecommercebackend.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderStatusRequestDto {

    @NotNull(message = "Status is required")
    private OrderStatus status;

}