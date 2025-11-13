package com.ordermanager.api.service;

import com.ordermanager.api.model.*;
import com.ordermanager.api.repository.CustomerRepository;
import com.ordermanager.api.repository.OrderItemRepository;
import com.ordermanager.api.repository.OrderRepository;
import com.ordermanager.api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // -------- 1. Creează comandă nouă --------
    public Order create(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found: " + customerId));

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    // -------- 2. Adaugă item într-o comandă --------
    public Order addItem(Long orderId, Long productId, int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.NEW) {
            throw new IllegalStateException("Order not editable in status: " + order.getStatus());
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + productId));

        // căutăm dacă produsul există deja în comandă
        OrderItem existingItem = order.getItems().stream()
                .filter(it -> it.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + qty);
            orderItemRepository.save(existingItem);
        } else {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(qty);
            item.setUnitPrice(product.getPrice());
            order.getItems().add(item);
            orderItemRepository.save(item);
        }

        return orderRepository.save(order);
    }

    // -------- 3. Total comandă --------
    public double total(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));

        return order.getItems().stream()
                .mapToDouble(it -> it.getUnitPrice() * it.getQuantity())
                .sum();
    }

    // -------- 4. Schimbare status (cu reguli ca înainte) --------
    public Order changeStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));

        OrderStatus cur = order.getStatus();

        boolean valid =
                (cur == OrderStatus.NEW && (newStatus == OrderStatus.PAID || newStatus == OrderStatus.CANCELLED)) ||
                (cur == OrderStatus.PAID && (newStatus == OrderStatus.SHIPPED || newStatus == OrderStatus.CANCELLED));

        if (!valid) {
            throw new IllegalStateException("Invalid transition: " + cur + " -> " + newStatus);
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    // -------- 5. Toate comenzile --------
    public List<Order> all() {
        return orderRepository.findAll();
    }

    // -------- 6. Comandă după id --------
    public Order byId(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + id));
    }

    // -------- 7. Comenzi după client --------
    public List<Order> byCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    // -------- 8. Comenzi după status --------
    public List<Order> byStatus(OrderStatus status) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getStatus() == status)
                .toList();
    }

    // -------- 9. Update cantitate item --------
    public Order updateItemQty(Long orderId, Long productId, int newQty) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.NEW) {
            throw new IllegalStateException("Order not editable in status: " + order.getStatus());
        }

        if (newQty < 0) {
            throw new IllegalArgumentException("Quantity must be >= 0");
        }

        OrderItem item = order.getItems().stream()
                .filter(it -> it.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product not in order: " + productId));

        if (newQty == 0) {
            order.getItems().remove(item);
            orderItemRepository.delete(item);
        } else {
            item.setQuantity(newQty);
            orderItemRepository.save(item);
        }

        return orderRepository.save(order);
    }
}
