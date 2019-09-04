package com.silver.bars.liveorderboard.service;

import com.silver.bars.liveorderboard.domain.Order;
import com.silver.bars.liveorderboard.domain.OrderType;
import com.silver.bars.liveorderboard.exceptions.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersServiceTest {

    private OrdersService underTest;

    @BeforeEach
    void setUp() {
        underTest = new OrdersService();
    }

    @Test
    @DisplayName("Should Create Order")
    void givenOrder_WhenCreateOrder_ThenAddOrderToCollectionAndReturnOrderId() {
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
    @DisplayName("Cancel Order by Order Id")
    void shouldCancelOrder() throws OrderNotFoundException {
        //Given
        Order order = testOrder();

        String orderId = underTest.createOrder(order);

        //When
        underTest.cancelOrder(orderId);

        //Then
        assertNull(underTest.getOrder(orderId));
    }

    @Test
    @DisplayName("Cancel Order by Order Id")
    void givenNonExistentOrderId_whenCancelOrder_thenThrowOrderNotFoundException() {
        //Given
        String orderId = "TestOrderId";
        String expectedMessage = "Order for orderId 'TestOrderId' is not found";

        OrderNotFoundException thrown =
                assertThrows(OrderNotFoundException.class,
                        //When
                        () -> underTest.cancelOrder(orderId),
                        "Expected OrderNotFoundException to be thrown");

        //Then
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void givenThreeOrdersCreated_whenGetAllOrders_thenReturnAllThreeOrders() {
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
                .quantity(BigDecimal.valueOf(12.34))
                .price(9876L)
                .orderType(OrderType.SELL)
                .build();
    }

    private Order testOrder2() {
        return Order.builder()
                .userId("user2")
                .quantity(BigDecimal.valueOf(56.78))
                .price(1234L)
                .orderType(OrderType.BUY)
                .build();
    }

    private Order testOrder3() {
        return Order.builder()
                .userId("user3")
                .quantity(BigDecimal.valueOf(19.82))
                .price(5678L)
                .orderType(OrderType.SELL)
                .build();
    }
}