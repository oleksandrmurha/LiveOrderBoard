package com.silver.bars.liveorderboard.service;

import com.silver.bars.liveorderboard.domain.Order;
import com.silver.bars.liveorderboard.domain.OrderType;
import com.silver.bars.liveorderboard.exceptions.OrderNotFoundException;
import com.silver.bars.liveorderboard.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;

    private static final Map<OrderType, Comparator<Long>> orderTypeComparatorMap =
            new HashMap<OrderType, Comparator<Long>>() {{
                put(OrderType.SELL, Comparator.naturalOrder());
                put(OrderType.BUY, Comparator.reverseOrder());
            }};

    public String createOrder(Order order) {
        return ordersRepository.createOrder(order);
    }

    public void cancelOrder(String orderId) throws OrderNotFoundException {
        Optional.ofNullable(ordersRepository.cancelOrder(orderId))
                .orElseThrow(() -> this.orderNotFoundException(orderId));
    }

    public Collection<Order> getAllOrders() {
        return ordersRepository.getAllOrders();
    }

    private OrderNotFoundException orderNotFoundException(String orderId) {
        return new OrderNotFoundException(String.format("Order for orderId '%s' is not found", orderId));
    }

    public Map<OrderType, List<String>> extractOrdersSummary(Collection<Order> allOrders) {
        return new HashMap<OrderType, List<String>>() {{
            put(OrderType.SELL, extractOrdersSummary(allOrders, OrderType.SELL));
            put(OrderType.BUY, extractOrdersSummary(allOrders, OrderType.BUY));
        }};
    }

    private List<String> extractOrdersSummary(Collection<Order> allOrders, OrderType orderType) {
        DecimalFormat df = new DecimalFormat("#.##");

        Map<Long, Double> filteredAndGroupedByPrice = Optional.ofNullable(allOrders)
                .orElse(Collections.emptyList())
                .stream()
                .filter(order -> orderType.equals(order.getOrderType()))
                .collect(groupingBy(Order::getPrice, summingDouble(Order::getQuantity)));

        return filteredAndGroupedByPrice.keySet()
                .stream()
                .sorted(orderTypeComparatorMap.get(orderType))
                .map(price -> String.format("%s kg for £%d", df.format(filteredAndGroupedByPrice.get(price)), price))
                .collect(toList());
    }
}
