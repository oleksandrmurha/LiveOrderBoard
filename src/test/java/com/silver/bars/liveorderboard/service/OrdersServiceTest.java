package com.silver.bars.liveorderboard.service;

import com.silver.bars.liveorderboard.domain.Order;
import com.silver.bars.liveorderboard.domain.OrderType;
import com.silver.bars.liveorderboard.exceptions.OrderNotFoundException;
import com.silver.bars.liveorderboard.repository.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrdersServiceTest {

    @Mock
    private OrdersRepository mockOrdersRepository;

    @InjectMocks
    private OrdersService underTest;

    @Test
    void givenOrder_WhenCreateOrder_ThenCallRepositoryCreateOrder() {
        //Given
        Order order = new Order();

        //When
        underTest.createOrder(order);

        //Then
        verify(mockOrdersRepository).createOrder(eq(order));
    }

    @Test
    void givenExistingOrderId_WhenCancelOrder_ThenCallRepositoryCancelOrder() throws OrderNotFoundException {
        //Given
        String orderId = "TestOrderId";
        doReturn(new Order()).when(mockOrdersRepository).cancelOrder(eq(orderId));

        //When
        underTest.cancelOrder(orderId);

        //Then
        verify(mockOrdersRepository).cancelOrder(eq(orderId));
    }

    @Test
    void givenNonExistentOrderId_WhenCancelOrder_ThenThrowOrderNotFoundException() {
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
    void givenNoOrders_whenExtractOrdersSummary_thenReturnEmptyListForBuyAndSellOrderTypes() {
        //When
        Map<OrderType, List<String>> ordersSummaryMap = underTest.extractOrdersSummary();

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
                sellOrder(10.20, 1L),
                buyOrder(20.10, 1L),
                sellOrder(30.40, 2L),
                buyOrder(40.50, 2L),
                sellOrder(50.60, 3L),
                buyOrder(60.70, 3L)
        );
        doReturn(allOrders).when(mockOrdersRepository).getAllOrders();

        //When
        Map<OrderType, List<String>> ordersSummaryMap = underTest.extractOrdersSummary();

        //Then
        assertEquals(2, ordersSummaryMap.size());
        assertEquals(3, ordersSummaryMap.get(OrderType.SELL).size());
        assertEquals("11.4 kg for £1", ordersSummaryMap.get(OrderType.SELL).get(0));
        assertEquals("33.8 kg for £2", ordersSummaryMap.get(OrderType.SELL).get(1));
        assertEquals("56.2 kg for £3", ordersSummaryMap.get(OrderType.SELL).get(2));

        assertEquals(3, ordersSummaryMap.get(OrderType.BUY).size());
        assertEquals("67.4 kg for £3", ordersSummaryMap.get(OrderType.BUY).get(0));
        assertEquals("45 kg for £2", ordersSummaryMap.get(OrderType.BUY).get(1));
        assertEquals("22.2 kg for £1", ordersSummaryMap.get(OrderType.BUY).get(2));
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