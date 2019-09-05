package com.silver.bars.liveorderboard.repository;

import com.silver.bars.liveorderboard.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrdersInMemoryRepository implements OrdersRepository {

    private Map<String, Order> ordersMap = new ConcurrentHashMap<>();

    @Override
    public String createOrder(Order order) {
        String orderId = generateOrderId();
        ordersMap.put(orderId, order);
        return orderId;
    }

    @Override
    public Order cancelOrder(String orderId) {
        return ordersMap.remove(orderId);
    }

    @Override
    public Order getOrder(String orderId) {
        return ordersMap.get(orderId);
    }

    @Override
    public Collection<Order> getAllOrders() {
        return ordersMap.values();
    }

    private String generateOrderId() {
        return UUID.randomUUID().toString();
    }
}
