package com.codewithbavly.ecommercebackend.controller;

import com.codewithbavly.ecommercebackend.dto.request.CreateOrderRequestDto;
import com.codewithbavly.ecommercebackend.dto.request.UpdateOrderStatusRequestDto;
import com.codewithbavly.ecommercebackend.dto.response.OrderResponseDto;
import com.codewithbavly.ecommercebackend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(
            @Valid @RequestBody CreateOrderRequestDto requestDto) {

        OrderResponseDto response = orderService.placeOrder(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders() {

        return ResponseEntity.ok(orderService.getMyOrders());

    }
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        return ResponseEntity.ok(authentication.getAuthorities());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequestDto requestDto) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(id, requestDto)
        );
    }

//    @Bean
//    CommandLineRunner printPassword(PasswordEncoder passwordEncoder) {
//        return args -> {
//            System.out.println(passwordEncoder.encode("admin123"));
//        };
//    }


}