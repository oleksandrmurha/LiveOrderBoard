package com.silver.bars.liveorderboard.repository;

import com.silver.bars.liveorderboard.domain.Order;

import java.util.Collection;

public interface OrdersRepository {

    String createOrder(Order order);

    Order cancelOrder(String orderId);

    Order getOrder(String orderId);

    Collection<Order> getAllOrders();
}
