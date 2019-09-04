package com.silver.bars.liveorderboard.controller;

import com.silver.bars.liveorderboard.domain.Order;
import com.silver.bars.liveorderboard.domain.OrderType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrdersControllerITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void given() {
        //Given
        Order order = testOrder();

        //When
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/orders", order, String.class);

        //Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void given1() {
        //Given
        Order order = new Order();

        //When
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/orders", order, String.class);

        //Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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