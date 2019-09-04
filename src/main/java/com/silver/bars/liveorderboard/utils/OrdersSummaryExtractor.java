package com.silver.bars.liveorderboard.utils;

import com.silver.bars.liveorderboard.domain.Order;
import com.silver.bars.liveorderboard.domain.OrderType;
import org.springframework.stereotype.Component;

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

@Component
public class OrdersSummaryExtractor {

    private static final Map<OrderType, Comparator<Long>> orderTypeComparatorMap =
            new HashMap<OrderType, Comparator<Long>>() {{
                put(OrderType.SELL, Comparator.naturalOrder());
                put(OrderType.BUY, Comparator.reverseOrder());
            }};

    public Map<OrderType, List<String>> extractOrdersSummary(Collection<Order> allOrders) {
        return new HashMap<OrderType, List<String>>() {{
            put(OrderType.SELL, extractOrdersSummary(allOrders, OrderType.SELL));
            put(OrderType.BUY, extractOrdersSummary(allOrders, OrderType.BUY));
        }};
    }

    private List<String> extractOrdersSummary(Collection<Order> allOrders, OrderType orderType) {
        Map<Long, Double> filteredAndGroupedByPrice = Optional.ofNullable(allOrders)
                .orElse(Collections.emptyList())
                .stream()
                .filter(order -> order.getOrderType().equals(orderType))
                .collect(groupingBy(Order::getPrice, summingDouble(Order::getQuantity)));

        return filteredAndGroupedByPrice.keySet()
                .stream()
                .sorted(orderTypeComparatorMap.get(orderType))
                .map(price -> String.format("%.2f kg for £%d", filteredAndGroupedByPrice.get(price), price))
                .collect(toList());
    }
}
