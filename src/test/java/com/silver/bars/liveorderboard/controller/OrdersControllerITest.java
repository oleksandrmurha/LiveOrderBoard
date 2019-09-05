package com.silver.bars.liveorderboard.controller;

import com.silver.bars.liveorderboard.domain.Order;
import com.silver.bars.liveorderboard.domain.OrderType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrdersControllerITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void givenOrder_whenPostToOrdersURL_thenStatusCodeIsCreated() {
        //Given
        Order order = sellOrder(12.34, 10);

        //When
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/orders", order, String.class);

        //Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void givenEmptyOrder_whenPostToOrdersURL_thenStatusCodeIsBadRequest() {
        //Given
        Order order = new Order();

        //When
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/orders", order, String.class);

        //Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void givenExistingOrderId_whenDeleteToOrdersURL_thenStatusCodeIsNoContent() {
        //Given
        Order order = sellOrder(12.34, 10);
        String orderId = restTemplate.postForEntity("/orders", order, String.class).getBody();

        //When
        ResponseEntity<Void> exchange = restTemplate.exchange("/orders?orderId=" + orderId, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        //Then
        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
    }

    @Test
    void givenNonExistentOrderId_whenDeleteToOrdersURL_thenStatusCodeIsNoContent() {
        //Given
        String orderId = "TestOrderId";

        //When
        ResponseEntity<Void> exchange = restTemplate.exchange("/orders?orderId=" + orderId, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode());
    }

    @Test
    void givenMultipleOrders_whenGetOrdersSummary_thenReturnOrdersSummary() {
        //Given
        restTemplate.postForEntity("/orders", buyOrder(1.01, 10), String.class);
        restTemplate.postForEntity("/orders", sellOrder(2.02, 20), String.class);
        restTemplate.postForEntity("/orders", buyOrder(3.3, 30), String.class);
        restTemplate.postForEntity("/orders", sellOrder(4.4, 40), String.class);
        restTemplate.postForEntity("/orders", buyOrder(10.10, 10), String.class);
        restTemplate.postForEntity("/orders", sellOrder(20.20, 20), String.class);

        //When
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity("/orders", Map.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, List<String>> body = (Map<String, List<String>>) responseEntity.getBody();

        assertEquals(2, body.size());

        assertEquals(2, body.get(OrderType.SELL.toString()).size());
        assertEquals("22.22 kg for £20", body.get(OrderType.SELL.toString()).get(0));
        assertEquals("4.4 kg for £40", body.get(OrderType.SELL.toString()).get(1));

        assertEquals(2, body.get(OrderType.BUY.toString()).size());
        assertEquals("3.3 kg for £30", body.get(OrderType.BUY.toString()).get(0));
        assertEquals("11.11 kg for £10", body.get(OrderType.BUY.toString()).get(1));
    }

    @Test
    void givenMultipleOrdersWithSomeCancelled_whenGetOrdersSummary_thenReturnOrdersSummaryWithoutCancelled() {
        //Given
        restTemplate.postForEntity("/orders", buyOrder(1.01, 10), String.class);
        restTemplate.postForEntity("/orders", sellOrder(2.02, 20), String.class);
        restTemplate.postForEntity("/orders", buyOrder(3.3, 30), String.class);
        restTemplate.postForEntity("/orders", sellOrder(4.4, 40), String.class);

        ResponseEntity<String> buyOrder = restTemplate.postForEntity("/orders", buyOrder(10.10, 10), String.class);
        ResponseEntity<String> sellOrder = restTemplate.postForEntity("/orders", sellOrder(20.20, 20), String.class);

        restTemplate.exchange("/orders?orderId=" + buyOrder.getBody(), HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        restTemplate.exchange("/orders?orderId=" + sellOrder.getBody(), HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        //When
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity("/orders", Map.class);

        //Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, List<String>> body = (Map<String, List<String>>) responseEntity.getBody();

        assertEquals(2, body.size());

        assertEquals(2, body.get(OrderType.SELL.toString()).size());
        assertEquals("2.02 kg for £20", body.get(OrderType.SELL.toString()).get(0));
        assertEquals("4.4 kg for £40", body.get(OrderType.SELL.toString()).get(1));

        assertEquals(2, body.get(OrderType.BUY.toString()).size());
        assertEquals("3.3 kg for £30", body.get(OrderType.BUY.toString()).get(0));
        assertEquals("1.01 kg for £10", body.get(OrderType.BUY.toString()).get(1));
    }

    private Order sellOrder(double quantity, long price) {
        return order(quantity, price, OrderType.SELL);
    }

    private Order buyOrder(double quantity, long price) {
        return order(quantity, price, OrderType.BUY);
    }

    private Order order(double quantity, long price, OrderType orderType) {
        return Order.builder()
                .userId(UUID.randomUUID().toString())
                .quantity(quantity)
                .price(price)
                .orderType(orderType)
                .build();
    }
}