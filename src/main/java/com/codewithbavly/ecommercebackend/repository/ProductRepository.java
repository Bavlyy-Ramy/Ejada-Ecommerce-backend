package com.codewithbavly.ecommercebackend.repository;

import com.codewithbavly.ecommercebackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
