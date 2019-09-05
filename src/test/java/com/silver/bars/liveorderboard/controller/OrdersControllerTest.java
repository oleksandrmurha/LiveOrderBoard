package com.silver.bars.liveorderboard.controller;

import com.silver.bars.liveorderboard.domain.Order;
import com.silver.bars.liveorderboard.exceptions.OrderNotFoundException;
import com.silver.bars.liveorderboard.service.OrdersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrdersControllerTest {

    @Mock
    private OrdersService mockOrdersService;

    @InjectMocks
    private OrdersController underTest;

    @Test
    void givenOrder_whenCreateOrder_ThenServiceCreateOrderShouldCalled() {
        //Given
        Order order = new Order();

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
    void whenGetOrdersSummary_TheServiceExtractOrdersSummaryShouldBeCalled() {
        //Then
        underTest.getOrdersSummary();

        //Then
        verify(mockOrdersService).extractOrdersSummary();
    }

}