package com.silver.bars.liveorderboard.repository;

import com.silver.bars.liveorderboard.domain.Order;
import com.silver.bars.liveorderboard.domain.OrderType;
import com.silver.bars.liveorderboard.exceptions.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersInMemoryRepositoryTest {

    private OrdersRepository underTest;

    @BeforeEach
    void setUp() {
        underTest = new OrdersInMemoryRepository();
    }

    @Test
    void givenOrder_WhenCreateOrder_ThenSaveOrderAndReturnOrderId() {
        //Given
        Order order = testOrder();

        //When
        String orderId = underTest.createOrder(order);

        //Then
        assertNotNull(orderId);
        assertEquals(order, underTest.getOrder(orderId));
    }

    @Test
    void givenExistingOrderId_whenGetOrder_thenReturnOrder() {
        //Given
        Order expectedOrder = testOrder();
        String orderId = underTest.createOrder(expectedOrder);

        //When
        Order actualOrder = underTest.getOrder(orderId);

        //Then
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void givenNonExistentOrderId_whenGetOrder_thenReturnNull() {
        //Given
        String orderId = "TestOrderId";

        //When
        Order actualOrder = underTest.getOrder(orderId);

        //Then
        assertNull(actualOrder);
    }

    @Test
    void givenExistingOrderId_whenCancelOrder_thenOrderIsRemoved() throws OrderNotFoundException {
        //Given
        Order order = testOrder();
        String orderId = underTest.createOrder(order);

        //When
        Order removedOrder = underTest.cancelOrder(orderId);

        //Then
        assertNotNull(removedOrder);
        assertNull(underTest.getOrder(orderId));
    }

    @Test
    void givenNonExistentOrderId_whenCancelOrder_thenReturnNull() {
        //Given
        String orderId = "TestOrderId";

        //When
        Order removedOrder = underTest.cancelOrder(orderId);

        //Then
        assertNull(removedOrder);
    }

    @Test
    void givenMultipleOrdersCreated_whenGetAllOrders_thenReturnAllOrders() {
        //Given
        Order order1 = testOrder();
        Order order2 = testOrder2();
        Order order3 = testOrder3();

        underTest.createOrder(order1);
        underTest.createOrder(order2);
        underTest.createOrder(order3);

        //When
        Collection<Order> allOrders = underTest.getAllOrders();

        //Then
        assertEquals(3, allOrders.size());
        assertTrue(allOrders.contains(order1));
        assertTrue(allOrders.contains(order2));
        assertTrue(allOrders.contains(order3));
    }

    private Order testOrder() {
        return Order.builder()
                .userId("user1")
                .quantity(12.34)
                .price(9876L)
                .orderType(OrderType.SELL)
                .build();
    }

    private Order testOrder2() {
        return Order.builder()
                .userId("user2")
                .quantity(56.78)
                .price(1234L)
                .orderType(OrderType.BUY)
                .build();
    }

    private Order testOrder3() {
        return Order.builder()
                .userId("user3")
                .quantity(19.82)
                .price(5678L)
                .orderType(OrderType.SELL)
                .build();
    }
}