package com.silver.bars.liveorderboard.service;

import com.silver.bars.liveorderboard.domain.Order;
import com.silver.bars.liveorderboard.exceptions.OrderNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrdersService {

    private Map<String, Order> ordersMap = new ConcurrentHashMap<>();

    public String createOrder(Order order) {
        String orderId = generateOrderId();
        ordersMap.put(orderId, order);
        return orderId;
    }

    public void cancelOrder(String orderId) throws OrderNotFoundException {
        Optional.ofNullable(ordersMap.remove(orderId))
                .orElseThrow(() -> this.orderNotFoundException(orderId));
    }

    public Order getOrder(String orderId) {
        return ordersMap.get(orderId);
    }

    public Collection<Order> getAllOrders() {
        return ordersMap.values();
    }

    private String generateOrderId() {
        return UUID.randomUUID().toString();
    }

    private OrderNotFoundException orderNotFoundException(String orderId) {
        return new OrderNotFoundException(String.format("Order for orderId '%s' is not found", orderId));
    }
}
