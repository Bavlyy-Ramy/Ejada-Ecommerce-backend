package com.codewithbavly.ecommercebackend.repository;

import com.codewithbavly.ecommercebackend.entity.Order;
import com.codewithbavly.ecommercebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

}