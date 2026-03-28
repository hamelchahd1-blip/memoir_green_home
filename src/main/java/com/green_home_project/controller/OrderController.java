package com.green_home_project.controller;

import com.green_home_project.dto.OrderDTO;
import com.green_home_project.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")

public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // BUY PLANT
    @PostMapping("/{plantId}")
    public OrderDTO createOrder(@PathVariable Long plantId) {
        return orderService.addOrder(plantId);
    }

    // MY ORDERS
    @GetMapping("/my-orders")
    public List<OrderDTO> myOrders() {
        return orderService.getMyOrders();
    }

    // SELLER ORDERS
    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/seller-orders")
    public List<OrderDTO> sellerOrders() {
        return orderService.getSellerOrders();
    }
    // ================= ACCEPT ORDER =================
    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/accept/{orderId}")
    public OrderDTO acceptOrder(@PathVariable Long orderId) {
        return orderService.acceptOrder(orderId);
    }

    // ================= REJECT ORDER =================
    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/reject/{orderId}")
    public OrderDTO rejectOrder(@PathVariable Long orderId) {
        return orderService.rejectOrder(orderId);
    }
    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/pay/{orderId}")
    public OrderDTO payOrder(@PathVariable Long orderId) {
        return orderService.payOrder(orderId);
    }
}
