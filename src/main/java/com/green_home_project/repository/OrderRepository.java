package com.green_home_project.repository;

import com.green_home_project.model.Order;
import com.green_home_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyer(User buyer);

    List<Order> findBySeller(User seller);
}