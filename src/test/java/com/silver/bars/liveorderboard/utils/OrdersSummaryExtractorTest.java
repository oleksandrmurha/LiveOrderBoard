package com.silver.bars.liveorderboard.utils;

import com.silver.bars.liveorderboard.domain.Order;
import com.silver.bars.liveorderboard.domain.OrderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrdersSummaryExtractorTest {

    private OrdersSummaryExtractor underTest;

    @BeforeEach
    void setUp() {
        underTest = new OrdersSummaryExtractor();
    }

    @Test
    void givenAllOrdersIsNull_whenExtractOrdersSummary_thenReturnEmptyListForBuyAndSellOrderTypes() {
        //Given
        Collection<Order> allOrders = null;

        //When
        Map<OrderType, List<String>> ordersSummaryMap = underTest.extractOrdersSummary(allOrders);

        //Then
        assertEquals(2, ordersSummaryMap.size());
        assertEquals(0, ordersSummaryMap.get(OrderType.SELL).size());
        assertEquals(0, ordersSummaryMap.get(OrderType.BUY).size());
    }

    @Test
    void givenOrdersWithDifferentOrderTypes_whenExtractOrdersSummary_thenReturnOrdersSummaryGroupByType() {
        //Given
        Collection<Order> allOrders = Arrays.asList(
                sellOrder(1.2, 1L),
                buyOrder(2.1, 1L),
                sellOrder(3.4, 2L),
                buyOrder(4.5, 2L),
                sellOrder(5.6, 3L),
                buyOrder(6.7, 3L),
                sellOrder(1.2, 1L),
                buyOrder(2.1, 1L),
                sellOrder(3.4, 2L),
                buyOrder(4.5, 2L),
                sellOrder(5.6, 3L),
                buyOrder(6.7, 3L)
        );

        //When
        Map<OrderType, List<String>> ordersSummaryMap = underTest.extractOrdersSummary(allOrders);

        //Then
        assertEquals(2, ordersSummaryMap.size());
        assertEquals(3, ordersSummaryMap.get(OrderType.SELL).size());
        assertEquals("2.40 kg for £1", ordersSummaryMap.get(OrderType.SELL).get(0));
        assertEquals("6.80 kg for £2", ordersSummaryMap.get(OrderType.SELL).get(1));
        assertEquals("11.20 kg for £3", ordersSummaryMap.get(OrderType.SELL).get(2));

        assertEquals(3, ordersSummaryMap.get(OrderType.BUY).size());
        assertEquals("13.40 kg for £3", ordersSummaryMap.get(OrderType.BUY).get(0));
        assertEquals("9.00 kg for £2", ordersSummaryMap.get(OrderType.BUY).get(1));
        assertEquals("4.20 kg for £1", ordersSummaryMap.get(OrderType.BUY).get(2));
    }

    private Order sellOrder(double quantity, long price) {
        return order(quantity, price, OrderType.SELL);
    }

    private Order buyOrder(double quantity, long price) {
        return order(quantity, price, OrderType.BUY);
    }

    private Order order(double quantity, long price, OrderType orderType) {
        return Order.builder()
                .userId(generateUserId())
                .quantity(quantity)
                .price(price)
                .orderType(orderType)
                .build();
    }

    private String generateUserId() {
        return UUID.randomUUID().toString();
    }
}