package com.green_home_project.service;

import com.green_home_project.dto.OrderDTO;
import com.green_home_project.dto.PaymentRequest;
import com.green_home_project.exception.ResourceNotFoundException;
import com.green_home_project.model.Order;

import com.green_home_project.model.OrderStatus;
import com.green_home_project.model.Plant;
import com.green_home_project.model.User;
import com.green_home_project.repository.OrderRepository;
import com.green_home_project.repository.PlantRepository;
import com.green_home_project.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final PaymentService paymentService;
    public OrderService(OrderRepository orderRepository,
                        PlantRepository plantRepository,
                        UserRepository userRepository,
                        NotificationService notificationService ,
                        PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.paymentService = paymentService;
    }

    // ================== ADD ORDER ==================
    public OrderDTO addOrder(Long plantId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User buyer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found"));

        User seller = plant.getUser();
        if (!seller.isCanSell()) {
            throw new RuntimeException("Seller is not active for selling");
        }

        Order order = new Order();
        order.setPlant(plant);
        order.setBuyer(buyer);
        order.setSeller(seller);
        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        // Notification for Seller
        notificationService.createNotification(
                seller,
                "لديك طلب جديد على النبات '" + plant.getName() + "'"
        );

        return convert(savedOrder);
    }

    // ================== GET MY ORDERS ==================
    public List<OrderDTO> getMyOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User buyer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByBuyer(buyer)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    // ================== GET SELLER ORDERS ==================
    public List<OrderDTO> getSellerOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findBySeller(seller)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    // ================= ACCEPT ORDER ==================
    public OrderDTO acceptOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!order.getSeller().getEmail().equals(email)) {
            throw new RuntimeException("You can only manage your own orders");
        }

        order.setStatus(OrderStatus.ACCEPTED);
        Order savedOrder = orderRepository.save(order);

        // Notification for Buyer
        notificationService.createNotification(
                order.getBuyer(),
                "طلبك على النبات '" + order.getPlant().getName() + "' تم قبوله"
        );

        return convert(savedOrder);
    }

    // ================= REJECT ORDER ==================
    public OrderDTO rejectOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!order.getSeller().getEmail().equals(email)) {
            throw new RuntimeException("You can only manage your own orders");
        }

        order.setStatus(OrderStatus.REJECTED);
        Order savedOrder = orderRepository.save(order);

        // Notification for Buyer
        notificationService.createNotification(
                order.getBuyer(),
                "طلبك على النبات '" + order.getPlant().getName() + "' تم رفضه"
        );

        return convert(savedOrder);
    }

    // ================== CONVERT TO DTO ==================
    private OrderDTO convert(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setPlantName(order.getPlant().getName());
        dto.setBuyerName(order.getBuyer().getName());
        dto.setSellerName(order.getSeller().getName());
        dto.setStatus(order.getStatus());
        return dto;
    }
    // ================= PAY ORDER =================
    public OrderDTO payOrder(Long orderId, PaymentRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        if (!order.getBuyer().getEmail().equals(email)) {
            throw new RuntimeException("You can only pay your own orders");
        }

        if (order.getStatus() != OrderStatus.ACCEPTED) {
            throw new RuntimeException("Order must be accepted first");
        }

        boolean paymentSuccess = paymentService.processPayment(
                request.getCardNumber(),
                request.getCvv()
        );

        if (!paymentSuccess) {
            throw new RuntimeException("Payment failed");
        }

        order.setStatus(OrderStatus.PAID);

        Order savedOrder = orderRepository.save(order);

        // 🔔 notification seller
        notificationService.createNotification(
                order.getSeller(),
                "تم دفع الطلب '" + order.getPlant().getName() + "' 💰"
        );

        return convert(savedOrder);
    }

}
