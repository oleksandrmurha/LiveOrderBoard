package com.silver.bars.liveorderboard.controller;

import com.silver.bars.liveorderboard.domain.Order;
import com.silver.bars.liveorderboard.domain.OrderType;
import com.silver.bars.liveorderboard.exceptions.OrderNotFoundException;
import com.silver.bars.liveorderboard.service.OrdersService;
import com.silver.bars.liveorderboard.utils.OrdersSummaryExtractor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrdersControllerTest {

    @Mock
    private OrdersService mockOrdersService;

    @Mock
    private OrdersSummaryExtractor mockOrdersSummaryExtractor;

    @InjectMocks
    private OrdersController underTest;

    @Test
    void givenOrder_whenCreateOrder_ThenServiceCreateOrderShouldCalled() {
        //Given
        Order order = testOrder();

        //Then
        underTest.createOrder(order);

        //Then
        verify(mockOrdersService).createOrder(eq(order));
    }

    @Test
    void givenOrderId_whenCancelOrder_ThenServiceCancelOrderShouldCalled() throws OrderNotFoundException {
        //Given
        String orderId = "Test Order Id";

        //Then
        underTest.cancelOrder(orderId);

        //Then
        verify(mockOrdersService).cancelOrder(eq(orderId));
    }

    @Test
    void whenGetOrdersSummary_TheOrdersSummaryExtractorShouldBeCalled() {
        //Given
        Collection<Order> orders = Collections.singletonList(testOrder());
        doReturn(orders).when(mockOrdersService).getAllOrders();

        //Then
        underTest.getOrdersSummary();

        //Then
        verify(mockOrdersService).getAllOrders();
        verify(mockOrdersSummaryExtractor).extractOrdersSummary(eq(orders));
    }

    private Order testOrder() {
        return Order.builder()
                .userId("user1")
                .quantity(12.34)
                .price(9876L)
                .orderType(OrderType.SELL)
                .build();
    }
}