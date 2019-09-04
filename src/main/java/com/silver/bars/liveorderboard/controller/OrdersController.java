package com.silver.bars.liveorderboard.controller;

import com.silver.bars.liveorderboard.domain.Order;
import com.silver.bars.liveorderboard.domain.OrderType;
import com.silver.bars.liveorderboard.exceptions.OrderNotFoundException;
import com.silver.bars.liveorderboard.service.OrdersService;
import com.silver.bars.liveorderboard.utils.OrdersSummaryExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;
    private final OrdersSummaryExtractor ordersSummaryExtractor;

    @PostMapping
    public String createOrder(Order order) {
        return ordersService.createOrder(order);
    }

    @DeleteMapping
    public void cancelOrder(String orderId) throws OrderNotFoundException {
        ordersService.cancelOrder(orderId);
    }

    @GetMapping
    public Map<OrderType, List<String>> getOrdersSummary() {
        return ordersSummaryExtractor.extractOrdersSummary(ordersService.getAllOrders());
    }
}
