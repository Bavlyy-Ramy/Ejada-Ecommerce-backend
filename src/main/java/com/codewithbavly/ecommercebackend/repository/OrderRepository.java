package com.codewithbavly.ecommercebackend.repository;

import com.codewithbavly.ecommercebackend.entity.Order;
import com.codewithbavly.ecommercebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

}