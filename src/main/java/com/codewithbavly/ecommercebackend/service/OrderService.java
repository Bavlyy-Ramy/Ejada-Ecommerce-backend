package com.codewithbavly.ecommercebackend.service;

import com.codewithbavly.ecommercebackend.dto.request.CreateOrderItemRequestDto;
import com.codewithbavly.ecommercebackend.dto.request.CreateOrderRequestDto;
import com.codewithbavly.ecommercebackend.dto.request.UpdateOrderStatusRequestDto;
import com.codewithbavly.ecommercebackend.dto.response.OrderItemResponseDto;
import com.codewithbavly.ecommercebackend.dto.response.OrderResponseDto;
import com.codewithbavly.ecommercebackend.entity.Order;
import com.codewithbavly.ecommercebackend.entity.OrderItem;
import com.codewithbavly.ecommercebackend.entity.Product;
import com.codewithbavly.ecommercebackend.entity.enums.OrderStatus;
import com.codewithbavly.ecommercebackend.exception.ResourceNotFoundException;
import com.codewithbavly.ecommercebackend.repository.OrderRepository;
import com.codewithbavly.ecommercebackend.repository.ProductRepository;
import com.codewithbavly.ecommercebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.codewithbavly.ecommercebackend.entity.User;
import com.codewithbavly.ecommercebackend.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.codewithbavly.ecommercebackend.exception.InsufficientStockException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser();
    }

    @Transactional
    public OrderResponseDto placeOrder(CreateOrderRequestDto requestDto) {

        User currentUser = getCurrentUser();

        Order order = new Order();

        order.setUser(currentUser);

        order.setStatus(OrderStatus.PENDING);

        order.setOrderDate(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.valueOf(0);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateOrderItemRequestDto itemDto : requestDto.getItems()) {
            //Find the Product
            Product product = productRepository.findById(itemDto.getProductId()).orElseThrow(() ->
                            new ResourceNotFoundException("Product not found with id: " + itemDto.getProductId()));

            //Check Stock
            if (product.getStockQuantity() < itemDto.getQuantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for product: " + product.getName()
                );
            }

            //Create OrderItem
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);

            orderItem.setProduct(product);

            orderItem.setQuantity(itemDto.getQuantity());

            orderItem.setPriceAtPurchase(product.getPrice());

            //Calculate Subtotal
            BigDecimal subtotal = product.getPrice() .multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            //Reduce Stock
            product.setStockQuantity(
                    product.getStockQuantity() - itemDto.getQuantity()
            );

            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return mapToResponseDto(savedOrder);

    }
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getMyOrders() {

        User currentUser = getCurrentUser();

        List<Order> orders = orderRepository.findByUser(currentUser);

        return orders.stream()
                .map(this::mapToResponseDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {

        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequestDto requestDto) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId
                        ));

        order.setStatus(requestDto.getStatus());

        Order updatedOrder = orderRepository.save(order);

        return mapToResponseDto(updatedOrder);
    }



    private OrderResponseDto mapToResponseDto(Order order) {

        return OrderResponseDto.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount().doubleValue())
                .items(
                        order.getOrderItems()
                                .stream()
                                .map(this::mapOrderItemToDto)
                                .toList()
                )
                .build();
    }

    private OrderItemResponseDto mapOrderItemToDto(OrderItem orderItem) {

        BigDecimal subtotal = orderItem.getPriceAtPurchase()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        return OrderItemResponseDto.builder()
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getPriceAtPurchase().doubleValue())
                .subtotal(subtotal.doubleValue())
                .build();
    }

}